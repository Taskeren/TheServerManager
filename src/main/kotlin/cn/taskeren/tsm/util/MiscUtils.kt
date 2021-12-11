package cn.taskeren.tsm.util

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Date.format(pattern: String = "yyyy-MM-dd HH:mm:ssZ"): String = SimpleDateFormat(pattern).format(this)

fun <T> List<T>.copy() = ArrayList(this)

fun File.file(name: String) = File(getOrCreateDirectory(), name)

fun File.folder(name: String) = File(getOrCreateDirectory(), name)