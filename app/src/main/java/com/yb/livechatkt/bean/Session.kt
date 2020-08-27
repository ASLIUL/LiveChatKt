package com.yb.livechatkt.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import java.io.Serializable

@Entity(tableName = "session")
class Session(
    @PrimaryKey
    var accid:String,
    var name:String,
    var lastMsg:String,
    var lastMsgTime: Long,
    var unRead:Int,
    var header:String,
    var sessionType:Int,
):Serializable{
    override fun toString(): String {
        return "Session(accid='$accid', name='$name', lastMsg='$lastMsg', lastMsgTime=$lastMsgTime, unRead=$unRead, header='$header', sessionType=$sessionType)"
    }
}