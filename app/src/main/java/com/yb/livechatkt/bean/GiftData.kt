package com.yb.livechatkt.bean

data class GiftData(val id:Int,val name:String,var gifts:MutableList<GiftItemData>?)

data class GiftItemData(
    val id: Int,
    val type:Int,
    val icon:String,
    val name: String,
    val gif:String,
    val img:String,
    val status:Int,
    val price:Double,
    val create_time:Long
    )