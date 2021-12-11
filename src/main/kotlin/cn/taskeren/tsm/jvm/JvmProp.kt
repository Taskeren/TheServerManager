package cn.taskeren.tsm.jvm

import cn.taskeren.tsm.util.mustExists
import java.io.File

data class JvmProp(val homePath: File, val jarPath: File = File(homePath, "/bin/java.exe").absoluteFile) {

	init {
		homePath.mustExists()
		jarPath.mustExists()
	}

}
