package cn.taskeren.tsm.mc.running

import cn.taskeren.tsm.TSMGlobal.tsmServerMcArgs

class McArgs(
	var noGui: Boolean? = null
) {

	fun noGui() {
		noGui = true
	}

	fun build(): List<String> {
		return buildList {
			if(noGui == true) {
				this += "nogui"
			}
		}
	}

}

/**
 * 构建 Minecraft 服务器参数
 * @param func 构建配置
 */
fun buildMcArgs(func: McArgs.() -> Unit): List<String> = McArgs().apply(tsmServerMcArgs).apply(func).build()