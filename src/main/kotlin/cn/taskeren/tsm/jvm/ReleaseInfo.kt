package cn.taskeren.tsm.jvm

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

private val logger: Logger = LogManager.getLogger()

class ReleaseInfo(releaseFile: File) {

	private val entries = mutableMapOf<String, String>()

	init {
		releaseFile.forEachLine {
			val s = it.split("=", limit = 2)
			val k = s[0].trim()
			var v = s[1].trim()

			if(k.isEmpty() || v.isEmpty()) {
				logger.error("Unable to read line $it")
			} else {
				if(v.startsWith('"') && v.endsWith('"')) {
					v = v.substring(1, v.length-1)
				}

				entries[k] = v
			}
		}
	}

	operator fun get(key: String) = entries[key]

}