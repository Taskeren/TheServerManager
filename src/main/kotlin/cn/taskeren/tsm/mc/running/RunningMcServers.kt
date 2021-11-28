@file:JvmName("RunningMcServerManager")

package cn.taskeren.tsm.mc.running

import cn.taskeren.tsm.mc.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.concurrent.thread

private val logger: Logger = LogManager.getLogger()

object RunningMcServers {

	private val goodbyeWords = listOf(
		"Farewell", "See you tomorrow!", "Well", "Minecraft", "Exactly!", "Devil(bad boi) May(after April) Cry(drops tear)"
	)

	/**
	 * @see start 添加
	 * @see RunningMcServer.onProcessExit 移除
	 */
	val servers = mutableListOf<RunningMcServer>()

	init {
		logger.info("Registering Server Stopping Hook")
		Runtime.getRuntime().addShutdownHook(thread(start = false) {
			if(servers.isEmpty()) {
				logger.info("All Servers has stopped before TSM exit")
			} else {
				logger.info("Stopping running servers(${servers.size})")
				servers.forEach {
					logger.debug("Stopping Server(${it.hashCode()}) on pid ${it.process.pid()}")
					it.stop()
				}
			}
		})
	}

}