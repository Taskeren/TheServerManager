package cn.taskeren.tsm.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.format(pattern: String = "yyyy-MM-dd HH:mm:ssZ"): String = SimpleDateFormat(pattern).format(this)