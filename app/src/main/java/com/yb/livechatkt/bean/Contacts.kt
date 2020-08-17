package com.yb.livechatkt.bean

data class Contacts(
    val id: Int,
    val page: PageX,
    val tid: Long,
    val tname: String


) {
    override fun toString(): String {
        return "Contacts(id=$id, page=${page.toString()}, tid=$tid, tname='$tname')"
    }
}