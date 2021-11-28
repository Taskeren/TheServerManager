package cn.taskeren.jarreader

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.lang.reflect.Method
import java.net.URLClassLoader
import java.util.jar.JarFile

private val logger: Logger = LogManager.getLogger()

class JarReader(private val file: File) {

	private val jarFile: JarFile

	var mainClass: String? = null

	var classLoader: URLClassLoader? = null

	init {
		if(!file.exists()) throw NoSuchFileException(file)

		jarFile = JarFile(file)

		jarFile.manifest.let {
			it.mainAttributes.forEach { key, value ->
				if(key.toString() == "Main-Class") {
					mainClass = value.toString()
					logger.debug("found Main-Class $value")
				}
			}
		}
	}

	fun load() {
		classLoader = URLClassLoader(arrayOf(file.toURI().toURL()))
	}

	fun forClassName(className: String): Class<*> {
		return classLoader?.loadClass(className) ?: throw NullPointerException()
	}

	val mainMethod: Method get() {
		return mainClass?.let {
			forClassName(it).getDeclaredMethod("main", Array<String>::class.java)
		} ?: throw NullPointerException("mainClass is null")
	}

}