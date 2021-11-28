package cn.taskeren.tsm.client.console

import cn.taskeren.tsm.TSMGlobal
import cn.taskeren.tsm.jvm.Jvm
import cn.taskeren.tsm.mc.*
import cn.taskeren.tsm.mc.running.kick
import cn.taskeren.tsm.util.pingMcServer
import org.apache.logging.log4j.LogManager
import java.nio.charset.Charset
import kotlin.system.exitProcess

private val logger = LogManager.getLogger()

/**
 * TODO:
 *
 * 当前这个类只是用来测试的，之后再完善整个控制台指令系统
 */
object ClientConsole {

	@Transient
	var looping = true

	val testJvm = Jvm("C:\\Program Files\\AdoptOpenJDK\\jdk-16.0.1.9-hotspot")
	val testServer = McServer("D:\\MinecraftServer\\1.17.1-paper\\paper-1.17.1-388.jar")

	fun doLoop() {
		while(looping) {
			runCatching {
				when(readLine()) {
					"q" -> looping = false
					"server" -> {
						testServer.start(testJvm) // .setupLoggingThread()
					}
					"stop" -> {
						testServer.runServer?.stop() ?: println("Server is not running")
					}
					"stop_end" -> {
						testServer.runServer?.stop(stopCommand = "end") ?: println("Server is not running")
					}
					"charset_gbk" -> {
						TSMGlobal.tsmServerEncoding = Charset.forName("gbk")
						println("Set Global Server Encoding to GBK(gb2312)")
					}
					"no_gui" -> {
						TSMGlobal.tsmServerDefaultMcArgs += "nogui"
						println("Added 'nogui' to Global Minecraft Arguments")
					}
					"modify_port_25565" -> {
						testServer.properties["server-port"] = "25565"
						testServer.saveProperties()
						println("Set port to 25565")
					}
					"modify_port_32767" -> {
						testServer.properties["server-port"] = "32767"
						testServer.saveProperties()
						println("Set port to 32767")
					}
					"kick_taskeren" -> {
						testServer.runServer?.kick("taskeren") ?: println("Server is not running")
					}
					"ping" -> {
						println(pingMcServer())
					}
					else -> {
						println("mismatched command")
					}
				}
			}.onFailure {
				logger.error("Error occurred while executing command", it)
			}
		}

		exitProcess(0)
	}

}