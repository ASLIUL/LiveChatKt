package com.yb.livechatkt.bean

import java.io.Serializable

data class PageX(
    val beginPos: Int,
    val curPage: Int,
    val pageCount: Int,
    val pageSize: Int,
    val rows: List<RowX>,
    val total: Int


) :Serializable{
    override fun toString(): String {
        return "PageX(beginPos=$beginPos, curPage=$curPage, pageCount=$pageCount, pageSize=$pageSize, rows=$rows, total=$total)"
    }
}