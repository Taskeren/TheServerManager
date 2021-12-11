package cn.taskeren.tsm

import cn.taskeren.tsm.mc.running.JvmArgs
import cn.taskeren.tsm.mc.running.McArgs
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

	val tsmServerMcArgs: (McArgs) -> Unit = {
		it.noGui = true
	}

	val tsmServerJvmArgs: (JvmArgs) -> Unit = {
		it.minMemo = "4G"
		it.maxMemo = "6G"
	}

}