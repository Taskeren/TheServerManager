package cn.taskeren.tsm.util

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ConnectException
import java.net.Socket

/**
 * 用来 ping 服务器的方法，似乎有点问题
 */
fun pingMcServer(host: String = "127.0.0.1", port: Int = 25565): List<String> {

	try {
		Socket(host, port).use { socket ->
			val sin = DataInputStream(socket.getInputStream())
			val out = DataOutputStream(socket.getOutputStream())

			out.write(0xFE)

			var b: Int
			val str = StringBuffer()

			while(true) {
				b = sin.read()
				if(b == -1) break

				if(b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
					str.append(b.toChar())
				}
			}

			return str.split("§")
		}
	} catch(ex: ConnectException) {
		return listOf(ex.message ?: ex.javaClass.canonicalName)
	}
}