package com.yb.livechatkt.net

object LiveChatUrl {

    //const val baseUrl = "http://192.168.0.103:9000"

    const val baseUrl = "http://www.yingbeikj.com:9000";
    //const val baseUrl = "http://zb.huayym.com";
    const val imgBaseUrl = "http://192.168.0.103:8080"

    //头像的baseUrl
    const val headerBaseUrl = "$baseUrl/image/redirect/"

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

    //获取客服账号
    const val getServiceChatAccount = "/service/user/getServiceUserOnline"

    //客服在线状态改变
    const val serviceUpdateState = "/service/user/modifyServiceUser/{status}"

    //客服获取招呼语
    const val serviceGetHiMessage = "/service/user/getMessage"

    //修改招呼语
    const val serviceUpdateHiMessage = "/service/user/modifyServiceUserMessage"

    //修改我的信息
    const val updateMyData = "/live/api/user/modifyUserInfor"

    const val updateMyHyNum = "/live/api/user/modifyUniId"

    //更改我的头像
    const val updateMyHeader = "/live/api/user/modifyHeadImg"

    //补全信息
    const val completeInformation = "/live/api/user/saveUserInfor"

    //根据liveId获取直播间详细信息
    const val getLiveRoomDataByLiveId = "/home/search/searchLiveById"

    //超管发信息上传文件
    const val adminUploadFile = "/superadmin/uploadMessageFile"

    //j检查当前客服是否在线
    const val judgeServiceIsOLine = "/service/user/judgeServiceIsOnline"

    //获取礼物类型
    const val getALlGiftTypeData = "/gift/findGiftType"

    //获取礼物根据类型
    const val getALlGiftByType = "/gift/findGift"

    //查询钱包
    const val getMyWalletBalance = "/gift/findWallet"

    //送礼物
    const val sendGiftInLive = "/gift/sendGift"



}