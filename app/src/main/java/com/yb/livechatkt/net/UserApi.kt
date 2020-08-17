package com.yb.livechatkt.net

import com.yb.livechatkt.bean.*
import okhttp3.RequestBody
import retrofit2.http.*

interface UserApi {

    //登陆 手机号密码登陆
    @POST(LiveChatUrl.login)
    suspend fun login(@Body hashMap: HashMap<String,Any>): Result<LoginDataBean>

    //获取首页直播列表
    @POST(LiveChatUrl.getHomeLiveList)
    suspend fun getHomeLive(@Body hashMap: HashMap<String, Any>) : Result<HomeLiveBean>

    //退出登录
    @PUT(LiveChatUrl.exitLogin)
    suspend fun exitLogin() : Result<Any>

    //获取我自己的信息
    @GET(LiveChatUrl.getMineData)
    suspend fun getMineData() : Result<Me>

    //获取联系人
    @GET(LiveChatUrl.getContacts)
    suspend fun getContacts(@Query("pageNo") pageNo:Int,@Query("pageSize") pageSize:Int) : Result<Contacts>

    //获取所有的群
    @GET(LiveChatUrl.getAllGroup)
    suspend fun getAllGroup() : Result<List<ServiceGroup>>

    //超管发送消息
    @POST(LiveChatUrl.adminSendMessage)
    suspend fun adminSendMessage(@Body body: RequestBody) : Result<Any>

}