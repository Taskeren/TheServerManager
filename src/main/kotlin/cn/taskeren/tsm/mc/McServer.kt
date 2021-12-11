package cn.taskeren.tsm.mc

import java.util.*

class McServer(val prop: ServerProp) {

	/**
	 * 运行中的服务器实例（服务器运行结束后会覆盖回null）
	 */
	var runServer: RunningMcServer? = null

	val running: Boolean get() = runServer?.running ?: false

	/**
	 * 获取服务器 server.properties 实例
	 */
	val properties: Properties by lazy { Properties().apply { this.load(prop.fileServerPath.properties.reader()) } }

	/**
	 * 保存 server.properties 内容
	 */
	fun saveProperties(comment: String = "Edited by The Server Manager") {
		properties.store(prop.fileServerJarPath.properties.writer(), comment)
	}

}