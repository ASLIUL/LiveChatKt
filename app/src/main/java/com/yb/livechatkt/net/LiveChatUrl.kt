package com.yb.livechatkt.net

object LiveChatUrl {

    const val baseUrl = "http://192.168.0.103:9000"

    //public static final String baseUrl = "http://www.moo9995.com:9000/";
    const val imgBaseUrl = "http://192.168.0.103:8080"

    //登陆
    const val login:String = "/api/loginByPassword"

    //获取首页直播列表
    const val getHomeLiveList = "/home/search/searchLive"

    //退出登录
    const val exitLogin = "/api/quit"

    //获取我自己的信息
    const val getMineData = "/live/api/user/findUserInfor"

    //获取联系人信息 //分页查询
    const val getContacts = "/live/api/link/findLinkUser"

    //超管获取所有群
    const val getAllGroup = "/superadmin/getGroupList"

    //超管发消息
    const val adminSendMessage = "/superadmin/sendGroupMessage"

}