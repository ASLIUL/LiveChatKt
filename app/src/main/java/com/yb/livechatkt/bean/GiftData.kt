package com.yb.livechatkt.bean

data class GiftData(val id:Int,val name:String,var gifts:List<GiftItemData>)

data class GiftItemData(
    val id: Int,
    val type:Int,
    val icon:String,
    val name: String,
    val gif:String,
    val img:String,
    val status:Int,
    val price:Double,
    val create_time:Long,
    var check:Int
    )

data class GiftRecords(val records:List<GiftItemData>)


class GiftAnimData(val id: Int,var num:Int,var username:String,val giftName:String,val giftImg:String,val header:String)