package ink.anur.pojo.common

import ink.anur.pojo.enumerate.RequestTypeEnum
import java.nio.ByteBuffer

/**
 * Created by Anur IjuoKaruKas on 2020/2/24
 *
 * 带有时间戳的消息，用于协调器之间通讯用
 */
abstract class AbstractTimedStruct : AbstractStruct() {

    companion object {
        val TimestampOffset = TypeOffset + TypeLength
        val TimestampLength = 8
        val OriginMessageOverhead = TimestampOffset + TimestampLength
    }

    fun init(byteBuffer: ByteBuffer, requestTypeEnum: RequestTypeEnum) {
        buffer = byteBuffer
        byteBuffer.position(TypeOffset)
        byteBuffer.putInt(requestTypeEnum.byteSign)
        byteBuffer.putLong(System.currentTimeMillis())
    }

    fun init(capacity: Int, requestTypeEnum: RequestTypeEnum, then: (ByteBuffer) -> Unit) {
        buffer = ByteBuffer.allocate(capacity)
        buffer!!.position(TypeOffset)
        buffer!!.putInt(requestTypeEnum.byteSign)
        buffer!!.putLong(System.currentTimeMillis())
        then.invoke(buffer!!)
        buffer!!.flip()
    }

    /**
     * 时间戳用于防止同一次请求的 “多次请求”，保证幂等性
     */
    fun getTimeMillis(): Long {
        return buffer!!.getLong(TimestampOffset)
    }
}