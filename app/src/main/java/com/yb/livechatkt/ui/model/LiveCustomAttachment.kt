package com.yb.livechatkt.ui.model

import com.alibaba.fastjson.JSONObject
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment

open abstract class LiveCustomAttachment(var type:Int) : MsgAttachment {


    fun fromJson(jsonObject: JSONObject){
        parseJsonData(jsonObject)
    }



    override fun toJson(send: Boolean): String {
        return LiveCustomAttachParser.packData(type,packData())
    }

    abstract fun parseJsonData(jsonObject: JSONObject)

    abstract fun  packData():JSONObject
}