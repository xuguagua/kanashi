package ink.anur.core.common

import ink.anur.common.KanashiExecutors
import ink.anur.mutex.ReentrantReadWriteLocker
import java.nio.ByteBuffer

/**
 * Created by Anur IjuoKaruKas on 2020/2/24
 *
 * 对于一个请求，总有发送回调，消息收到回调等等配置，就是通过这个来完成的
 */
class RequestExtProcessor(
    private val howToConsumeResponse: ((ByteBuffer) -> Unit)? = null,
    val requestProcessType: RequestProcessType = RequestProcessType.SEND_ONCE) : ReentrantReadWriteLocker() {

    /**
     * 是否已经完成了这个请求过程（包括接收response）
     */
    @Volatile
    private var complete = false

    /**
     * 是否孩子处于需要发送的状态
     */
    fun inFlight(): Boolean = !complete

    /**
     * 已经完成了此任务
     */
    fun complete() = writeLockSupplier {
        if (!complete) {
            complete = true
        }
    }

    /**
     * 已经完成了此任务
     */
    fun complete(byteBuffer: ByteBuffer) = writeLockSupplier {
        if (!complete) {
            complete = true

            KanashiExecutors.execute(Runnable {
                howToConsumeResponse?.invoke(byteBuffer)
            })
        }
    }
}