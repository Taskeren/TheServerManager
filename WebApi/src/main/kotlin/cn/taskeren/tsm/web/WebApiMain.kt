package cn.taskeren.tsm.web

import cn.taskeren.tsm.data.json.JsonDataProvider
import cn.taskeren.tsm.web.component.HelloWorldSchema
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import java.io.File

val dataProvider = JsonDataProvider(File("./.tsm"))

fun main() {

	val app = Javalin.create {
		it.enableDevLogging()

		it.showJavalinBanner = false
		it.defaultContentType = "application/json"
		it.enableCorsForAllOrigins()
	}

	app.start(8080)

	app.routes {
		get("/") {
			it.json(HelloWorldSchema("TSM/WebApi@v1.0"))
		}

		crud("/servers/{id}", ServerCrud)
		post("/servers/{id}/start") { ServerCrud.start(it, it.pathParam("id")) }
		post("/servers/{id}/stop") { ServerCrud.stop(it, it.pathParam("id")) }
		ws("/servers/{id}/logs") { ServerCrud.wsLog(it) }

		crud("/jvm/{id}", JvmCrud)

	}.exception(MissingKotlinParameterException::class.java) { ex, ctx ->
		ctx.makeInvalidRequestResponse(100, "${ex.parameter.name} 参数丢失", ctx.body())
	}.exception(Exception::class.java) { ex, ctx ->
		ctx.makeInternalErrorResponse(ex)
		ex.printStackTrace()
	}

}