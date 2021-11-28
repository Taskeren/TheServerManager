package cn.taskeren.tsm.mc.running

import cn.taskeren.tsm.mc.RunningMcServer

// TODO: 添加其他常用的指令

fun RunningMcServer.kick(playerName: String) = send("kick $playerName")