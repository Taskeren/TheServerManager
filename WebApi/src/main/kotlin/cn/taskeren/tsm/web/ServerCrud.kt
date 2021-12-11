package cn.taskeren.tsm.web

import cn.taskeren.tsm.data.getServer
import cn.taskeren.tsm.mc.*
import cn.taskeren.tsm.util.getValidJvm
import cn.taskeren.tsm.web.component.*
import cn.taskeren.tsm.web.component.response.makeResponse
import cn.taskeren.tsm.web.component.ws.ServerPacket
import cn.taskeren.tsm.web.component.ws.WelcomePacket
import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.ServiceUnavailableResponse
import io.javalin.websocket.WsConfig
import org.eclipse.jetty.websocket.api.CloseStatus

object ServerCrud : CrudHandler {

	override fun create(ctx: Context) {
		val prop = ctx.bodyAsClass<ServerProp>()

		val server = McServer(prop)
		dataProvider.servers += server
		dataProvider.save()
		ctx.makeSuccessResponse(server.toView())
	}

	override fun delete(ctx: Context, resourceId: String) {
		val inst = dataProvider.servers.find { it.prop.uuid.toString() == resourceId }

		if(inst == null) {
			ctx.makeNotFoundResponse("server not found")
		} else {
			dataProvider.servers.remove(inst)
			dataProvider.save()
			inst.runServer?.stop()
			ctx.makeSuccessResponse(inst.toView())
		}
	}

	override fun getAll(ctx: Context) {
		ctx.json(makeResponse(0, data = dataProvider.servers.map { it.toTinyView() }))
	}

	override fun getOne(ctx: Context, resourceId: String) {
		val s = dataProvider.servers.firstOrNull { it.prop.uuid.toString() == resourceId }
		if(s == null) {
			ctx.makeNotFoundResponse("server not found")
		} else {
			ctx.makeSuccessResponse(s)
		}
	}

	override fun update(ctx: Context, resourceId: String) {
		throw ServiceUnavailableResponse()
	}

	fun start(ctx: Context, resourceId: String) {
		val s = dataProvider.getServer(resourceId)
		if(s == null) {
			ctx.makeNotFoundResponse("server not found")
		} else {
			runCatching {
				s.start(s.prop.getValidJvm(dataProvider))
			}.onSuccess {
				ctx.makeSuccessResponse(s.toRunningView())
			}.onFailure {
				ctx.makeInternalErrorResponse(501, "Server has already started")
			}
		}
	}

	fun stop(ctx: Context, resourceId: String) {
		val s = dataProvider.getServer(resourceId)
		if(s == null) {
			ctx.makeNotFoundResponse("server not found")
		} else {
			if(s.running) {
				s.runServer?.stop()
				ctx.makeSuccessResponse(s.toTinyView())
			} else {
				ctx.makeInvalidRequestResponse(600, "server not running", s.toTinyView())
			}
		}
	}

	fun wsLog(cfg: WsConfig) {
		cfg.onConnect { ctx ->
			val id = ctx.pathParam("id")
			val server = dataProvider.getServer { it.prop.uuid.toString() == id }

			if(server == null) {
				ctx.send(WelcomePacket(1, "Server Not Found"))
				ctx.session.close(CloseStatus(1, "Server Not Found"))
			} else {
				val runningServer = server.runServer
				if(runningServer == null) {
					ctx.send(WelcomePacket(2, "Server Not Running"))
					ctx.session.close(CloseStatus(2, "Server Not Running"))
				} else {
					runningServer.initLogReaderThread()
					runningServer.logReaders["websocket"] = {
						ctx.send(ServerPacket(it))
					}
					ctx.send(WelcomePacket(0, "Initiated"))
				}
			}
		}

		cfg.onClose { ctx ->
			val id = ctx.pathParam("id")
			val server = dataProvider.getServer { it.prop.uuid.toString() == id }

			// 从日志订阅列表移除
			server?.runServer?.logReaders?.remove("websocket")
		}
	}
}