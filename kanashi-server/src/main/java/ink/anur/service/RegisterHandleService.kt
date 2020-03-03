package ink.anur.service

import ink.anur.core.central.common.AbstractRequestMapping
import ink.anur.core.central.core.RequestProcessCentreService
import ink.anur.inject.NigateBean
import ink.anur.inject.NigateInject
import ink.anur.io.common.channel.ChannelService
import ink.anur.struct.Register
import ink.anur.struct.RegisterResponse
import ink.anur.struct.common.AbstractStruct
import ink.anur.struct.enumerate.RequestTypeEnum
import io.netty.channel.Channel
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

/**
 * Created by Anur IjuoKaruKas on 2020/2/25
 *
 * 专门用于处理注册请求
 */
@NigateBean
class RegisterHandleService : AbstractRequestMapping() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @NigateInject
    private lateinit var channelService: ChannelService

    @NigateInject
    private lateinit var msgCenterService: RequestProcessCentreService


    override fun typeSupport(): RequestTypeEnum = RequestTypeEnum.REGISTER

    override fun handleRequest(fromServer: String, msg: ByteBuffer, channel: Channel) {
        var sign = 0
        try {
            sign = msg.getInt(AbstractStruct.TypeOffset)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val typeEnum = RequestTypeEnum.parseByByteSign(sign)

        if (typeEnum != RequestTypeEnum.REGISTER) {
            logger.error("????????????怎么发生的???????????????")
        }

        val register = Register(msg)

        channelService
            .getChannelHolder(ChannelService.ChannelType.COORDINATE)
            .register(register.getServerName(), channel)
        logger.info("协调节点 {} 已注册到本节点", register.getServerName())
        msgCenterService.send(fromServer, RegisterResponse())
    }
}