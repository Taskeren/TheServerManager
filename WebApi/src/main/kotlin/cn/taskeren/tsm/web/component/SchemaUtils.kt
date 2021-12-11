package cn.taskeren.tsm.web.component

import cn.taskeren.tsm.mc.McServer
import cn.taskeren.tsm.web.component.schema.*

fun McServer.toView(): ServerView = this.prop
fun McServer.toTinyView(): ServerTinyView = ServerTinyView(prop.uuid, prop.name, prop.fileServerJarPath)
fun McServer.toRunningView(): ServerRunningView = ServerRunningView(prop.uuid, prop.name, prop.fileServerPath, runServer!!.pid)