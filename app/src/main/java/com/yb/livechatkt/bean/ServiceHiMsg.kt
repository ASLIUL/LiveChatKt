package com.yb.livechatkt.bean

data class ServiceHiMsg(
    val create_time: Long,
    val id: Int,
    val message: String,
    val modify_time: Long,
    val user_id: Int
)