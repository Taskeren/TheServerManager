package cn.taskeren.tsm.web

import cn.taskeren.tsm.web.component.response.makeResponse
import io.javalin.http.Context
import io.javalin.http.HttpCode

fun <Data> Context.makeSuccessResponse(data: Data?) =
	json(makeResponse(0, data))

fun Context.makeNotFoundResponse(message: String) {
	status(HttpCode.NOT_FOUND)
	json(makeResponse(400, message))
}

fun Context.makeInvalidRequestResponse(code: Int, message: String, requested: Any?) {
	status(HttpCode.INTERNAL_SERVER_ERROR)
	json(makeResponse(code, requested, message))
}

fun Context.makeInternalErrorResponse(code: Int, message: String) {
	status(HttpCode.INTERNAL_SERVER_ERROR)
	json(makeResponse(code, message))
}

fun Context.makeInternalErrorResponse(ex: Exception) {
	status(HttpCode.INTERNAL_SERVER_ERROR)
	json(makeResponse(500, ex.stackTraceToString(), ex.message.toString()))
}