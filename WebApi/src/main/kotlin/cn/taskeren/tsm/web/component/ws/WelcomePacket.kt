package cn.taskeren.tsm.web.component.ws

data class WelcomePacket(val status: Int, val message: String): Packet(1)