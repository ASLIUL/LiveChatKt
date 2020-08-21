package com.yb.livechatkt.ui.model

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser
import java.lang.Exception

class LiveCustomAttachParser : MsgAttachmentParser {

    companion object {

        val KEY_TYPE = "type"
        val KEY_DATA = "data"

        fun packData(type: Int, data: JSONObject): String {
            var jsonObject = JSONObject()
            jsonObject.put(KEY_TYPE, type)
            jsonObject.put(KEY_DATA, data)
            return jsonObject.toString()
        }
    }



    override fun parse(attach: String?): MsgAttachment {
        var liveCustomAttachment:LiveCustomAttachment? = null
        try {
            var jsonObject = JSON.parseObject(attach)
            val type = jsonObject.getInteger(KEY_TYPE)
            val data = jsonObject.getJSONObject(KEY_DATA)
            when(type){
                1 -> liveCustomAttachment = ShareLiveMessageAttachment()
            }
            liveCustomAttachment?.fromJson(data)

        }catch (e:Exception){
            e.printStackTrace()
        }
        return liveCustomAttachment!!

    }


}