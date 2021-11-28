package cn.taskeren.tsm.client

import cn.taskeren.tsm.client.console.ClientConsole
import cn.taskeren.tsm.util.format
import org.apache.logging.log4j.LogManager
import java.util.*

private val logger = LogManager.getLogger("ClientBootstrap")

fun main() {
	logger.info("${Date().format()} is a nice day.")
	ClientConsole.doLoop()
}