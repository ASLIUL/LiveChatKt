package com.yb.livechatkt.bean

data class ChatMessage(val code: Int,val msg: String,val data: Any?)


data class ChatRoomTextMessage(val code:Int, val msg:String, var time:Long, val data: RoomTextMessage)
data class RoomTextMessage(val connect:String,val username:String,val userId:String,val chatAccount:String,val level:Int,val userType:Int)

data class ChatRoomGiftMessage(val code:Int, val msg:String, var time:Long, val data: RoomGiftMessage)
data class RoomGiftMessage(val giftid:Int, val giftimg:String,val giftname:String,var giftnum:Int,val username:String,val userId:String,val chatAccount:String,val level:Int,val userType:Int)