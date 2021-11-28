package cn.taskeren.tsm.mc

import cn.taskeren.tsm.util.mustEndsWith
import cn.taskeren.tsm.util.mustExists
import java.io.File
import java.util.*

class McServer(
	val jarFile: File,

	/**
	 * 运行中的服务器实例（服务器运行结束后会覆盖回null）
	 */
	var runServer: RunningMcServer? = null,

	/**
	 * 关服指令
	 */
	var stopCommand: String = "stop"
) {

	constructor(jarFileString: String) : this(File(jarFileString).mustExists().mustEndsWith("jar"))

	var serverDir: ServerDirectory = jarFile.parentFile
		set(value) {
			// 禁止启动服务器后修改位置
			if(running) { error("this server has already started.") }
			field = value
		}

	val running: Boolean get() = runServer?.running ?: false

	/**
	 * 获取服务器 server.properties 实例
	 */
	val properties: Properties by lazy { Properties().apply { this.load(serverDir.properties.reader()) } }

	/**
	 * 保存 server.properties 内容
	 */
	fun saveProperties(comment: String = "Edited by The Server Manager") {
		properties.store(serverDir.properties.writer(), comment)
	}

}