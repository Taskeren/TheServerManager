@file:OptIn(ExperimentalStdlibApi::class)
@file:Suppress("SpellCheckingInspection")

package cn.taskeren.tsm.mc.running

import cn.taskeren.tsm.TSMGlobal.tsmServerJvmArgs

// -XX:+UseG1GC
// -XX:+UseFastAccessorMethods
// -XX:+OptimizeStringConcat
// -XX:MetaspaceSize=1024m
// -XX:MaxMetaspaceSize=2048m
// -XX:+AggressiveOpts
// -XX:MaxGCPauseMillis=10
// -XX:+UseStringDeduplication
class JvmArgs(
	var maxMemo: String? = null,
	var minMemo: String? = null,
	var useG1GC: Boolean? = null,
	var useFastAccessorMethods: Boolean? = null,
	var optimizeStringConcat: Boolean? = null,
	var metaspaceSize: String? = null,
	var maxMetaspaceSize: String? = null,
	var aggressiveOpts: Boolean? = null,
	var maxGCPauseMillis: Int? = null,
	var useStringDeduplication: Boolean? = null,

	var useSystemProxies: Boolean? = null
) {

	fun build(): List<String> {
		return buildList {
			if(maxMemo != null) {
				this += "-Xmx$maxMemo"
			}
			if(minMemo != null) {
				this += "-Xms$minMemo"
			}
			if(useG1GC == true) {
				this += "-XX:+UseG1GC"
			}
			if(useFastAccessorMethods == true) {
				this += "-XX:+UseFastAccessorMethods"
			}
			if(optimizeStringConcat == true) {
				this += "-XX:+OptimizeStringConcat"
			}
			if(metaspaceSize != null) {
				this += "-XX:MetaspaceSize=$metaspaceSize"
			}
			if(maxMetaspaceSize != null) {
				this += "-XX:MaxMetaspaceSize=$maxMetaspaceSize"
			}
			if(aggressiveOpts == true) {
				this += "-XX:+AggressiveOpts"
			}
			if(maxGCPauseMillis != null) {
				this += "-XX:MaxGCPauseMillis=$maxGCPauseMillis"
			}
			if(useStringDeduplication == true) {
				this += "-XX:+UseStringDeduplication"
			}
			if(useSystemProxies == true) {
				this += "-Djava.net.useSystemProxies=true"
			}
		}
	}

	/**
	 * 使用 Thermos 官方启动参数
	 */
	fun useThermosConfig() {
		maxMemo = "6G"
		minMemo = "4G"
		useG1GC = true
		useFastAccessorMethods = true
		optimizeStringConcat = true
		metaspaceSize = "1G"
		maxMetaspaceSize = "2G"
		aggressiveOpts = true
		maxGCPauseMillis = 10
		useStringDeduplication = true
	}

	fun useDefaultConfig() {
		maxMemo = "6G"
		minMemo = "4G"
	}

}

/**
 * 构建 Jvm 启动参数
 * @param func 构建配置
 */
fun buildJvmArgs(func: JvmArgs.() -> Unit): List<String> = JvmArgs().apply(tsmServerJvmArgs).apply(func).build()