package cn.taskeren.tsm.mc

import cn.taskeren.tsm.mc.running.RunningMcServers
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.BufferedWriter
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

/**
 * 正在运行的 Minecraft 服务器类
 *
 * [启动服务器][cn.taskeren.tsm.mc.start]
 * [关闭服务器][cn.taskeren.tsm.mc.stop]
 */
class RunningMcServer internal constructor(
	internal val process: Process,
	internal val server: McServer,

	internal val encoding: Charset
) {

	val logger: Logger = LogManager.getLogger(this.javaClass)

	/**
	 * 日志扫描器（用于获取服务器输出的日志）
	 */
	val logScanner: Scanner by lazy { Scanner(process.inputStream, encoding) }

	/**
	 * 指令输入器（用于发送指令信息）
	 */
	val commandWriter: BufferedWriter by lazy { process.outputStream.bufferedWriter(encoding) }

	val running: Boolean get() = process.isAlive

	val pid: Long get() = process.pid()

	var logReaderThread: Thread? = null

	/**
	 * 日志监听器
	 */
	val logReaders = mutableMapOf<Any, (String) -> Unit>()

	init {
		onProcessBegin()
	}

	/**
	 * 向服务器发送文字（控制台）
	 */
	fun send(str: String) {
		logger.debug("Send $str to Server(${hashCode()})")
		commandWriter.write(str + "\n")
		commandWriter.flush()
	}

	/**
	 * 强制结束服务器进程
	 * @see Process.destroy
	 */
	fun terminate() {
		process.destroy()
	}

	internal fun onProcessBegin() {
		// 与静态服务器绑定
		server.runServer = this

		// 进程退出执行相关方法
		process.onExit().thenRun(this::onProcessExit)
	}

	/**
	 * 当进程退出时被调用
	 */
	internal fun onProcessExit() {
		logger.info("Server(${hashCode()}) has fully exited")

		// 与静态服务器取消绑定
		server.runServer = null

		// 移除实例列表
		RunningMcServers.servers -= this
	}

	@Throws(IllegalStateException::class)
	fun initLogReaderThread() {
		if(logReaderThread == null) {
			logger.info("LogReader for Server ${server.prop.name}(PID=$pid) has initiated")
			logReaderThread = thread(isDaemon = true) {
				while(server.running) {
					if(logScanner.hasNextLine()) {
						val line = logScanner.nextLine()
						logReaders.forEach { (_, listener) -> listener(line) }
					}
				}
			}
		}
	}

}