package ink.anur.log.prelog

import ink.anur.log.common.LogItemAndOffset

/**
 * Created by Anur IjuoKaruKas on 2019/7/12
 *
 * 预日志的内容
 */
class PreLogMeta(val startOffset: Long, val endOffset: Long, val oao: Collection<LogItemAndOffset>)