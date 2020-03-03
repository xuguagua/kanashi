package ink.anur.service

import ink.anur.struct.enumerate.RequestTypeEnum
import ink.anur.core.central.common.AbstractRequestMapping
import ink.anur.core.raft.RaftCenterController
import ink.anur.inject.NigateBean
import ink.anur.inject.NigateInject
import ink.anur.struct.coordinate.Voting
import io.netty.channel.Channel
import java.nio.ByteBuffer

/**
 * Created by Anur IjuoKaruKas on 2020/2/27
 *
 * 专门处理拉票的处理器
 */
@NigateBean
class VotingHandleService : AbstractRequestMapping() {

    @NigateInject
    private lateinit var raftCenterController: RaftCenterController

    override fun typeSupport(): RequestTypeEnum {
        return RequestTypeEnum.VOTING
    }

    override fun handleRequest(fromServer: String, msg: ByteBuffer, channel: Channel) {
        val voting = Voting(msg)
        raftCenterController.receiveVote(fromServer, voting)
    }
}