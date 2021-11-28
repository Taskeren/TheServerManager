import cn.taskeren.tsm.jvm.Jvm
import cn.taskeren.tsm.mc.*
import cn.taskeren.tsm.mc.running.buildJvmArgs
import cn.taskeren.tsm.mc.running.buildMcArgs
import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

class TestMcServerStart {

	val logger = LogManager.getLogger()

	val jvm = Jvm("C:\\Program Files\\AdoptOpenJDK\\jdk-16.0.1.9-hotspot")

	@Test
	fun testJvm() {
		Jvm("C:\\Program Files\\AdoptOpenJDK\\jdk-16.0.1.9-hotspot").let {
			println(it.versionInCommand)
			println(it.versionFull)
			println(it.version)
		}

		Jvm("C:\\Program Files\\Java\\jre1.8.0_311").let {
			println(it.versionInCommand)
			println(it.versionFull)
			println(it.version)
		}
	}

	@Test
	fun testStarter() {
		val server = McServer(
			"D:\\MinecraftServer\\1.17.1-paper\\paper-1.17.1-100.jar"
		).start(
			jvm = jvm,
			jvmArgs = buildJvmArgs { useDefaultConfig() },
			mcArgs = buildMcArgs { noGui() },
			encoding = Charset.forName("GBK")
		)

		logger.info("PID {}", server.process.pid())

		// 启用日志输出线程
		server.setupLoggingThread() { logger.info(it) }

		logger.info("Running")

		try {
			Thread.sleep(3*60*1000)
		} catch(interrupt: InterruptedException) {
			logger.info("Interrupted, server has shut down.")
			return
		}

		server.stop()

		logger.info("Terminating")
		server.terminate()
		logger.info("Done")
	}

}