package cn.taskeren.tsm.jvm

import java.io.File

data class Jvm(val prop: JvmProp) {

	val release: ReleaseInfo = ReleaseInfo(File(prop.homePath, "release"))

	/**
	 * 通过 'java -version' 获取到的版本信息
	 */
	val versionInCommand: String by lazy {
		ProcessBuilder().command(prop.jarPath.toString(), "--version")
			.start().inputStream.bufferedReader().lines().toList().joinToString("\n")
	}

	/**
	 * 获取 Java 版本（例如：7，8，11，16）
	 */
	val version: String by lazy { getVersion0() }

	/**
	 * 获取完全的 Java 版本（例如：“1.8.0_311”，“16.0.1”）
	 */
	val versionFull: String get() = release["JAVA_VERSION"] ?: error("version unset")

	/**
	 * 获取JVM的版本
	 */
	private fun getVersion0(): String {
		val vPart = versionFull.split('.')
		return if(vPart[0] == "1") { // 1.x.y => x
			vPart[1]
		} else { // x.y.z => x
			vPart[0]
		}
	}

}

object JvmManager {

	private val jvmList = mutableListOf<Jvm>()

	fun addJvm(jvm: Jvm) = jvmList.add(jvm)
	fun removeJvm(jvm: Jvm) = jvmList.remove(jvm)

	fun getJvm(filter: (Jvm) -> Boolean = {true}) = jvmList.filter(filter)
	fun getJvm(version: Int = 8) = getJvm { version.toString() == it.version }

	val size: Int get() = jvmList.size
	operator fun get(index: Int) = jvmList[index]

	fun getJvmList(): List<Jvm> = ArrayList(jvmList)

}