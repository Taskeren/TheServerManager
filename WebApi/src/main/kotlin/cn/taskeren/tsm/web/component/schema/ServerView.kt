package cn.taskeren.tsm.web.component.schema

import cn.taskeren.tsm.mc.ServerProp
import java.io.File
import java.util.*

typealias ServerView = ServerProp

data class ServerTinyView(val uuid: UUID, val name: String, val path: File)

data class ServerRunningView(val uuid: UUID, val name: String, val path: File, val pid: Long)