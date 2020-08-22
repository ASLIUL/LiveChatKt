package com.yb.livechatkt.bean

data class ChatRoomCustomMsg(val code:Int,val msg:String,val msgData: MsgData) {

}
data class MsgData(val connect:String,val username:String,val userId:String,val accid:String,val level:Int,val userType:Int)