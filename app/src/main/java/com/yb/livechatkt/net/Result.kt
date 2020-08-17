package com.yb.livechatkt.net

class Result<T>(code:Int,msg:String,data:T?) {

    var code:Int = code
    var msg:String = msg
    var data:T? = data

}