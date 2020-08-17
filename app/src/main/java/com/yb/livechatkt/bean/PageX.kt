package com.yb.livechatkt.bean

data class PageX(
    val beginPos: Int,
    val curPage: Int,
    val pageCount: Int,
    val pageSize: Int,
    val rows: List<RowX>,
    val total: Int


) {
    override fun toString(): String {
        return "PageX(beginPos=$beginPos, curPage=$curPage, pageCount=$pageCount, pageSize=$pageSize, rows=$rows, total=$total)"
    }
}