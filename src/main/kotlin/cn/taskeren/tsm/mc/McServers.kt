package cn.taskeren.tsm.mc

import cn.taskeren.tsm.TSMGlobal
import cn.taskeren.tsm.jvm.Jvm
import cn.taskeren.tsm.mc.running.*
import cn.taskeren.tsm.util.getOrCreateDirectory
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * 根据给定的 Jvm 和参数启动服务器
 */
fun McServer.start(
	jvm: Jvm,
	jvmArgs: List<String> = buildJvmArgs {  },
	mcArgs: List<String> = buildMcArgs {  },
	serverDirectory: File = this.serverDir,
	encoding: Charset = TSMGlobal.tsmServerEncoding
): RunningMcServer {
	if(running) {
		error("Server(${runServer!!.hashCode()}) has started on pid ${runServer!!.process.pid()}")
	}

	val commandSplit = mutableListOf<String>()

	// JVM
	commandSplit += jvm.execFile.absolutePath

	// JVM args
	commandSplit += jvmArgs

	// Minecraft Jar
	commandSplit += "-jar"
	commandSplit += this.jarFile.absolutePath

	// Minecraft args
	commandSplit += mcArgs

	val process = ProcessBuilder(commandSplit).apply {
		this.redirectErrorStream(true)
		this.directory(serverDirectory) // 设置服务器运行位置
	}.start()

	return RunningMcServer(process, this, encoding).apply {
		// 添加到实例列表
		RunningMcServers.servers += this

		logger.info("{}", commandSplit)

		logger.info("Server(${hashCode()}) has started on pid ${process.pid()}")
	}
}

/**
 * 启用服务器日志输出线程
 * @param start 是否立即启动
 * @param outputMethod 日志消息输出方式
 */
fun RunningMcServer.setupLoggingThread(
	start: Boolean = true,
	outputMethod: (String) -> Unit = { println(it) }
) =
	thread(start = start, isDaemon = true) {
		while(this@setupLoggingThread.running) {
			if(logScanner.hasNextLine()) {
				outputMethod(logScanner.nextLine())
			}
		}
	}

/**
 * 关闭服务器
 * @param hardMode 是否强制杀死进程
 */
fun RunningMcServer.stop(
	hardMode: Boolean = false,
	waitForSec: Long = 10,
	stopCommand: String = "stop" // 关闭服务器的指令，部分服务器不以 'stop' 为关服指令，例如 BungeeCord 以 'end' 作为关闭指令。
) {
	logger.info("Stopping Server(${hashCode()}) by ${if(hardMode) "Process" else "Command ($stopCommand)"}")
	if(!hardMode) {
		send(stopCommand)
		if(!process.waitFor(waitForSec, TimeUnit.SECONDS)) { // 进程没有退出
			logger.warn("Failed to stop Server(${hashCode()}) by Command (spend time out of 10sec), triggering 'Process.destroy()'.")
			stop(true)
		}
	} else {
		terminate()
		if(!process.waitFor(waitForSec, TimeUnit.SECONDS)) {
			logger.error("Failed to destroy Server(${hashCode()}) on pid ${process.pid()}")
		}
	}
}

// Server Files

typealias ServerDirectory = File

val ServerDirectory.eula: File get() = File(this, "eula.txt")

/**
 * 获取 'server.properties' 文件
 */
val ServerDirectory.properties: File get() = File(this, "server.properties") // .mustExists() - BungeeCord 可能没有这个文件

/**
 * 获取 '/mods' 文件夹
 */
val ServerDirectory.modsDir: File get() = File(this, "mods/").getOrCreateDirectory()

/**
 * 获取 '/plugins' 文件夹
 */
val ServerDirectory.pluginsDir: File get() = File(this, "plugins/").getOrCreateDirectory()
