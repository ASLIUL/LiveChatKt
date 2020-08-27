package com.yb.livechatkt.bean

import com.yb.livechatkt.R

class PlugBean(val action:ActionType) {
}
enum class ActionType(val actionType:Int,val actionName:Int,val resId:Int){
    ALUBM(1,R.string.album, R.drawable.ic_album_black),
    SHOT(2,R.string.shot, R.drawable.ic_shot_black),
    VIDEO(3,R.string.video, R.drawable.ic_video_black)
}