package cn.taskeren.tsm.web.component.response

interface ResponseObject

data class NoDataResponseObject(
	val code: Int,
	val message: String
) : ResponseObject

data class DataResponseObject<Data>(
	val code: Int,
	val message: String,
	val data: Data
) : ResponseObject

data class DataListResponseObject<Data>(
	val code: Int,
	val message: String,
	val data: Collection<Data>,

	// For developers to check the list
	val length: Int = data.size
) : ResponseObject

fun makeResponse(code: Int, message: String = getDefaultMessageForCode(code)) =
	NoDataResponseObject(code, message)

fun <Data> makeResponse(code: Int, data: Data?, message: String = getDefaultMessageForCode(code)) =
	if(data == null) makeResponse(code, message) else DataResponseObject(code, message, data)

fun <Data> makeResponse(code: Int, data: Collection<Data>, message: String = getDefaultMessageForCode(code)) =
	DataListResponseObject(code, message, data)

private fun getDefaultMessageForCode(code: Int) = when(code) {
	0 -> "Success"
	else -> error("No defined message for code $code!")
}