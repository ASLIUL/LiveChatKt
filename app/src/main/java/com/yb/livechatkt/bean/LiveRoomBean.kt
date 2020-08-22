package com.yb.livechatkt.bean

data class LiveRoomBean(
    val cover_img: String,
    val fansNum: Int,
    val headImg: String,
    val hlsPullUrl: String,
    val httpPullUrl: String,
    val id: Int,
    val isFocus: Int,
    val name: String,
    val room_id: Int,
    val rtmpPullUrl: String,
    val userName: String,
    val user_id: Int
)