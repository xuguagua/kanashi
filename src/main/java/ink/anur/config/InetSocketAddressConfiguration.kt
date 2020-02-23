package ink.anur.config

import com.anur.config.common.ConfigHelper
import com.anur.config.common.ConfigurationEnum
import com.anur.core.coordinate.model.kanashiNode
import com.anur.exception.ApplicationConfigException
import com.anur.exception.kanashiException
import com.anur.util.ChannelManager
import ink.anur.config.common.ConfigHelper
import ink.anur.config.common.ConfigurationEnum
import ink.anur.core.struct.KanashiNode
import ink.anur.exception.ApplicationConfigException

/**
 * Created by Anur IjuoKaruKas on 2019/7/5
 *
 * 网络相关配置，都可以从这里获取
 */
object InetSocketAddressConfiguration : ConfigHelper() {

    private var me: KanashiNode? = null

    fun init(serverName: String?) {
        val name = serverName ?: getConfig(ConfigurationEnum.SERVER_NAME) { unChange -> unChange } as String
        if (name == ChannelManager.CoordinateLeaderSign) {
            throw ApplicationConfigException(" 'Leader' 为关键词，节点不能命名为这个")
        }
        me = getNode(name)

        if (me == KanashiNode.NOT_EXIST) {
            throw ApplicationConfigException("服务名未正确配置，或者该服务不存在于服务配置列表中")
        }
    }

    fun getServerElectionPort(): Int {
        return me!!.electionPort
    }

    fun getServerCoordinatePort(): Int {
        return me!!.coordinatePort
    }

    fun getServerName(): String {
        return me!!.serverName
    }

    fun getCluster(): List<KanashiNode> {
        return getConfigSimilar(ConfigurationEnum.CLIENT_ADDR) { pair ->
            val serverName = pair.key
            val split = pair.value
                .split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            KanashiNode(serverName, split[0], Integer.valueOf(split[1]), Integer.valueOf(split[2]))
        } as List<KanashiNode>
    }

    fun getNode(serverName: String?): KanashiNode {
        return getCluster().associateBy { kanashiLegal: KanashiNode -> kanashiLegal.serverName }[serverName] ?: KanashiNode.NOT_EXIST
    }
}
