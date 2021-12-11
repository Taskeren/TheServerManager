package cn.taskeren.tsm.data.json

import cn.taskeren.tsm.data.DataProvider
import cn.taskeren.tsm.jvm.Jvm
import cn.taskeren.tsm.jvm.JvmProp
import cn.taskeren.tsm.mc.McServer
import cn.taskeren.tsm.mc.ServerProp
import cn.taskeren.tsm.util.file
import cn.taskeren.tsm.util.getOrCreateDirectory
import com.alibaba.fastjson.JSON
import org.apache.logging.log4j.LogManager
import java.io.File

private val logger = LogManager.getLogger()

class JsonDataProvider(val dataFolder: File) : DataProvider {

	override val servers: MutableList<McServer> = mutableListOf()
	override val jvmList: MutableList<Jvm> = mutableListOf()

	init {
		dataFolder.getOrCreateDirectory()
		load()
	}

	fun save() {
		dataFolder.file("servers.json").writeText(JSON.toJSONString(servers.map { it.prop }))
		dataFolder.file("jvm.json").writeText(JSON.toJSONString(jvmList.map { it.prop }))
	}

	fun load() {
		servers += JSON.parseArray(dataFolder.file("servers.json").readText(), ServerProp::class.java)?.map { McServer(it) } ?: listOf()
		jvmList += JSON.parseArray(dataFolder.file("jvm.json").readText(), JvmProp::class.java)?.map { Jvm(it) } ?: listOf()
	}

}