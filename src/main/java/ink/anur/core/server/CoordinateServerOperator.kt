package ink.anur.core.server

import ink.anur.common.KanashiRunnable
import ink.anur.common.Shutdownable
import ink.anur.common.pool.DriverPool
import ink.anur.common.struct.common.AbstractStruct
import ink.anur.common.struct.enumerate.OperationTypeEnum
import ink.anur.core.struct.CoordinateRequest
import ink.anur.inject.NigateBean
import ink.anur.inject.NigateInject
import ink.anur.inject.PostConstruct
import ink.anur.io.common.channel.ChannelService
import ink.anur.io.common.ShutDownHooker
import ink.anur.io.server.CoordinateServer
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Anur IjuoKaruKas on 2020/2/22
 *
 * 集群内通讯、协调服务器操作类服务端，负责协调相关的业务
 */
@NigateBean
class CoordinateServerOperator : KanashiRunnable(), Shutdownable {

    @NigateInject
    private lateinit var channelService: ChannelService

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 协调服务端
     */
    private var coordinateServer: CoordinateServer

    @PostConstruct
    private fun init() = this.start()

    init {
        val sdh = ShutDownHooker("终止协调服务器的套接字接口 8080 的监听！")

        DriverPool.register(CoordinateRequest.javaClass,
            8,
            300,
            TimeUnit.MILLISECONDS,
            {},
            {}
        )

        this.coordinateServer = CoordinateServer(8080,
            sdh,
            /*
             * 定义 CoordinateServer 如何消费来自 client 的消息，这里直接将解码后的 msg 丢入 HandlerPool
             */
            { ctx, msg ->
                var sign = 0
                try {
                    sign = msg.getInt(AbstractStruct.TypeOffset)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val typeEnum = OperationTypeEnum.parseByByteSign(sign)
                DriverPool.offer(CoordinateRequest(msg, typeEnum, ctx.channel()))
            },
            {})
    }

//    /**
//     * Coordinate 断开连接时，需要从 ChannelManager 移除管理
//     */
//    internal class UnRegister : ChannelInboundHandlerAdapter() {
//
//        @Throws(Exception::class)
//        override fun channelInactive(ctx: ChannelHandlerContext) {
//            super.channelInactive(ctx)
//            ChannelManager.getInstance(ChannelType.COORDINATE)
//                .unRegister(ctx.channel())
//        }
//    }

    override fun shutDown() {
        coordinateServer.shutDown()
    }

    override fun run() {
        logger.info("协调服务器正在启动...")
        coordinateServer.start()
    }
}