package com.yb.livechatkt.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session")
class Session(
    @PrimaryKey
    var accid:String,
    var name:String,
    var lastMsg:String,
    var lastMsgTime: Long,
    var unRead:Int,
    var header:String,
    var sessionType:Int
)