package cn.taskeren.tsm.web.component.request

data class McServerCreateRequest(val serverJarPath: String, val jvmVersion: Int? = null)