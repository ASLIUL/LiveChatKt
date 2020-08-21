package com.yb.livechatkt.ui.model

import com.alibaba.fastjson.JSONObject
import com.yb.livechatkt.bean.LiveShareMessageBean
import com.yb.livechatkt.util.NetConstant

class ShareLiveMessageAttachment() : LiveCustomAttachment(NetConstant.SHARE_LIVE_MESSAGE){

    private val KEY_DATA = "data"

    var liveShareMessageBean:LiveShareMessageBean? = null

    constructor(liveShareMessageBean: LiveShareMessageBean) : this(){
        this.liveShareMessageBean = liveShareMessageBean
    }


    override fun parseJsonData(jsonObject: JSONObject) {
        liveShareMessageBean = jsonObject.getObject(KEY_DATA,LiveShareMessageBean::class.java)
    }

    override fun packData(): JSONObject {
        var  jsonObject = JSONObject()
        jsonObject.put(KEY_DATA,liveShareMessageBean)
        return jsonObject
    }
}