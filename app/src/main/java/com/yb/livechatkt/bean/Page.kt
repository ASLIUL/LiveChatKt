package com.yb.livechatkt.bean

data class Page(
    val beginPos: Int,
    val curPage: Int,
    val pageCount: Int,
    val pageSize: Int,
    val rows: List<Row>,
    val total: Int
)