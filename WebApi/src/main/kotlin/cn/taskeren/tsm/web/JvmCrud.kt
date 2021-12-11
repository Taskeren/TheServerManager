package cn.taskeren.tsm.web

import cn.taskeren.tsm.data.getJvm
import cn.taskeren.tsm.jvm.Jvm
import cn.taskeren.tsm.jvm.JvmProp
import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.ServiceUnavailableResponse

object JvmCrud: CrudHandler {

	override fun create(ctx: Context) {
		val jvmProp = ctx.bodyAsClass<JvmProp>()

		val jvm = Jvm(jvmProp)
		dataProvider.jvmList += jvm
		dataProvider.save()
		ctx.makeSuccessResponse(jvm)
	}

	override fun delete(ctx: Context, resourceId: String) {
		val jvm = dataProvider.getJvm { it.hashCode().toString() == resourceId }

		if(jvm == null) {
			ctx.makeNotFoundResponse("jvm not found")
		} else {
			dataProvider.jvmList -= jvm
			dataProvider.save()
			ctx.makeSuccessResponse(jvm)
		}
	}

	override fun getAll(ctx: Context) {
		ctx.makeSuccessResponse(dataProvider.jvmList)
	}

	override fun getOne(ctx: Context, resourceId: String) {
		val jvm = dataProvider.getJvm { it.hashCode().toString() == resourceId }
		if(jvm == null) {
			ctx.makeNotFoundResponse("jvm not found")
		} else {
			ctx.makeSuccessResponse(jvm)
		}
	}

	override fun update(ctx: Context, resourceId: String) {
		throw ServiceUnavailableResponse()
	}
}