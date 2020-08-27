package com.yb.livechatkt.bean

data class ChatRoomCustomMsg(val code:Int, val msg:String, var time:Long, val data: MsgData) {

}
data class MsgData(val connect:String,val username:String,val userId:String,val chatAccount:String,val level:Int,val userType:Int)