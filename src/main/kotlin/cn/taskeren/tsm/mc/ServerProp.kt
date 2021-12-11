package cn.taskeren.tsm.mc

import cn.taskeren.tsm.util.mustEndsWith
import cn.taskeren.tsm.util.mustExists
import java.io.File
import java.util.*

/**
 * 服务器静态配置
 *
 * 当 [jvmHash] 为空时，会读取 [jvmVersion] 寻找合适的虚拟机启动，若都为空则使用默认虚拟机。
 *
 * @param name 服务器名称
 * @param uuid 服务器唯一识别码
 * @param serverPath 服务器文件夹位置
 * @param serverJarPath 服务器 Jar 位置
 * @param jvmHash JVM 实例哈希值
 * @param jvmVersion 需求的 JVM 版本
 * @param commandStop 关闭服务器指令
 * @param timeDestroy 强制结束进程前等待的时间（秒）
 */
data class ServerProp(
	val name: String,
	val uuid: UUID = UUID.randomUUID(),

	// Server Properties
	val serverPath: String,
	val serverJarPath: String,

	// JVM Properties
	val jvmHash: Int? = null,
	val jvmVersion: Int? = null,

	// Other Settings
	var commandStop: String = "stop",

	var timeDestroy: Long = 10
) {

	val fileServerPath get() = File(serverPath)
	val fileServerJarPath get() = File(serverJarPath)

	init {
		fileServerPath.mustExists()
		fileServerJarPath.mustExists().mustEndsWith("jar")
	}
}
