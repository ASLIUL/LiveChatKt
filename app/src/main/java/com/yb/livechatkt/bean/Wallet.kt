package com.yb.livechatkt.bean

data class Wallet(
    val balance: Double,
    val create_time: Long,
    val earnings: Int,
    val id: Int,
    val spending: Int,
    val user_id: Int
)