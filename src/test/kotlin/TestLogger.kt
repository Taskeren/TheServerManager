import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Test

class TestLogger {

	@Test
	fun testLogger() {

		val logger = LogManager.getLogger()

		logger.info("1")
		logger.warn("Warning: 2")
		logger.error("Error: 3")

	}

}