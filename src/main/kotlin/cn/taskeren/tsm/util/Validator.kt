package cn.taskeren.tsm.util

import java.io.File

fun File.mustExists(): File = apply {
	if(!this.exists()) {
		error("$this not exists")
	}
}

fun File.mustEndsWith(extensionName: String) = apply {
	if(this.extension != extensionName) {
		error("$this not ends with $extensionName")
	}
}

/**
 * 确保文件或目录存在，若不存在自动建立
 * @param isFile 是否是文件
 */
fun File.getOrCreate(isFile: Boolean = true): File = apply {
	if(!this.exists()) {
		if(isFile) {
			this.parentFile.getOrCreate(false)
			this.createNewFile()
		} else {
			this.mkdirs()
		}
	}
}

/**
 * @see getOrCreate
 */
fun File.getOrCreateFile() = getOrCreate(true)

/**
 * @see getOrCreate
 */
fun File.getOrCreateDirectory() = getOrCreate(false)