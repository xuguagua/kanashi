/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ink.anur.log.common

import ink.anur.pojo.log.common.GenerationAndOffset
import ink.anur.pojo.log.base.LogItem

/**
 * Created by Anur IjuoKaruKas on 2019/10/11
 *
 * 此数据类型用于向引擎提交数据
 *
 * msgTime 用于返回 response
 *
 * 没有gao 代表这是一条查询指令 没有必要保存
 */
class EngineProcessEntry(val logItem: LogItem, val GAO: GenerationAndOffset? = null)