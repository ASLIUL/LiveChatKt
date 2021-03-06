package com.yb.livechatkt.util

object NetConstant {

    //edit sharepreferences key
    val spSaveKey = "live-chat"

    val PLEASRINPUTPHONE:String = "请输入用户名"


    //网络相关
    val responseDefaultCode:Int = 100
    val responseSuccessCode:Int = 200
    val responseErrorCode:Int = 1
    val responseNoDataCode = 100010
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

    //发起会话的key 1,单聊 2，群聊 3 客服单聊
    const val SESSION_TYPE = "session_type"

    const val SESSION_P2P = 1
    const val SESSION_TEAM = 2
    const val SESSION_SERVICE = 3
    //普通用户向客服会话请求
    const val SESSION_HAS_SERVICE_SESSION = 4
    //客服向普通用户的会话请求
    const val SESSION_NORMAL_USER_SESSION = 5

    //会话数据的key
    const val CONVERSATION_DATA = "conversation_data"

    //自定义消息类型
    const val SHARE_LIVE_MESSAGE = 1

    const val PLEASE_INPUT_CONNECT = "输入点内容吧"

    //地区key
    const val PROVINCE_ID = "provinceId"
    const val CITY_ID = "cityId"
    const val COUNTY_ID = "countyId"
    const val PROVINCE_NAME = "provinceName"
    const val CITY_NAME = "cityName"
    const val COUNTY_NAME = "countyName"

    //limit
    const val CONNECT_LENGTH_NICKNAME:Int = 10
    const val CONNECT_LENGTH_HYNUM = 20
    const val CONNECT_LENGTH_SIGN = 100

    const val SHARE_LIVE_USER_ID = "share_user_id"
    const val LIVE_ID = "live_id"

    const val NOT_INPUT_SOMETHING = "不可以发送空消息哦"
    const val EXIT_LIVE = 100

    const val SEND_GIFT_LIVE_MSG = 300

    const val CHAT_ROOM_INPUT_CONNECT = "chat_room_connect"
}