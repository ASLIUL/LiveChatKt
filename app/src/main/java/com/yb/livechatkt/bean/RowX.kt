package com.yb.livechatkt.bean

data class RowX(
    val accid: String,
    val heamImg: String,
    val id: Int,
    val name: String,
    val sex: Int,
    val signature: String


) {
    override fun toString(): String {
        return "RowX(accid='$accid', heamImg='$heamImg', id=$id, name='$name', sex=$sex, signature='$signature')"
    }
}