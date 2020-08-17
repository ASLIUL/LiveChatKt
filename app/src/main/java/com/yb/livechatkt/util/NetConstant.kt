package com.yb.livechatkt.util

object NetConstant {

    //edit sharepreferences key
    val spSaveKey = "live-chat"

    val PLEASRINPUTPHONE:String = "请输入用户名"


    //网络相关
    val responseDefaultCode:Int = 100
    val responseSuccessCode:Int = 200
    val responseErrorCode:Int = 1
    val reponseDefaultMsg:String = "操作成功"
    val reponseErrorMsg:String = "网络错误，请稍后重试"
    val responseTokenFailedCode:Int = 400000;
    val responseTokenFailedMsg:String = "登录失效，请重新登录";

    val API_CONNECT_TIME_OUT:Long = 10
    val API_READ_TIME_OUT:Long = 10
    val API_WRITE_TIME_OUT:Long = 10

    const val IM_APP_KEY = "5c35b9162148f25b125e4af2d052134d"

    const val TEST_TOAST:String = "测试通知"

    //超管选择群信息后传到下一页面的key
    const val ANIM_CHOOSER_GROUP_DATA = "anim_choose_group_data"
    const val PLEASE_CHOOSE_GROUP_DATA = "请选择要发送的群"

}