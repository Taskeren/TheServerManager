package cn.taskeren.tsm

import java.io.File
import java.nio.charset.Charset

object TSMGlobal {

	var tsmFolder: File? = null
		get() {
			return field?.let {
				if(it.exists()) {
					it
				} else {
					throw NoSuchFileException(it)
				}
			} ?: throw NullPointerException("tsmFolder")
		}

	var tsmServerEncoding: Charset = Charset.defaultCharset()

	val tsmServerDefaultMcArgs = mutableListOf<String>()
	val tsmServerDefaultJvmArgs = mutableListOf<String>()

}

fun main() = cn.taskeren.tsm.client.main()