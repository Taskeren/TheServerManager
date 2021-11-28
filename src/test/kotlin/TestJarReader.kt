import cn.taskeren.jarreader.JarReader
import org.junit.jupiter.api.Test
import java.io.File

class TestJarReader {

	@Test
	fun testManifest() {
		JarReader(File("D:\\MinecraftServer\\1.17.1-paper\\paper-1.17.1-100.jar")).apply {
			load()
			mainMethod.invoke(null, arrayOf<String>())
		}
	}

}