package com.yb.livechatkt.bean

import java.io.Serializable

data class RowX(
    val accid: String,
    val heamImg: String,
    val id: Int,
    val name: String,
    val sex: Int,
    val signature: String


) :Serializable{
    override fun toString(): String {
        return "RowX(accid='$accid', heamImg='$heamImg', id=$id, name='$name', sex=$sex, signature='$signature')"
    }
}