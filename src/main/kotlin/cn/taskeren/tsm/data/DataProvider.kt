package cn.taskeren.tsm.data

import cn.taskeren.tsm.jvm.Jvm
import cn.taskeren.tsm.mc.McServer

interface DataProvider {

	val servers: MutableList<McServer>

	val jvmList: MutableList<Jvm>

	val runningServers get() = servers.filter { it.running }

}

fun DataProvider.getServer(uuid: String) = servers.find { it.prop.uuid.toString() == uuid }

fun DataProvider.getServer(filter: (McServer) -> Boolean = {true}) = servers.firstOrNull(filter)
fun DataProvider.getJvm(filter: (Jvm) -> Boolean = {true}) = jvmList.firstOrNull(filter)