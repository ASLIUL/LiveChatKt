package com.yb.livechatkt.net

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.yb.livechatkt.bean.*
import okhttp3.MultipartBody
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

    //获取客服账号
    @GET(LiveChatUrl.getServiceChatAccount)
    suspend fun getServiceChatAccount() : Result<ServicePerson>

    //更改客服在线状态
    @PUT(LiveChatUrl.serviceUpdateState)
    suspend fun updateServiceState(@Path("status") status:String) : Result<Any>

    //获取招呼语
    @GET(LiveChatUrl.serviceGetHiMessage)
    suspend fun getServiceHiMessage() :Result<List<ServiceHiMsg>>

    //修改招呼语
    @POST(LiveChatUrl.serviceUpdateHiMessage)
    suspend fun updateServiceHiMessage(@Body requestBody: RequestBody) : Result<Any>

    //更改我自己的信息
    @Headers("Content-Type: application/json")
    @PUT(LiveChatUrl.updateMyData)
    suspend fun updateMyData(@Body requestBody: RequestBody):Result<Any>


    //更改我的花漾账号
    @Headers("Content-Type: application/json")
    @PUT(LiveChatUrl.updateMyHyNum)
    suspend fun updateMyHyNum(@Query("uni_id")uniId:String):Result<Any>


    //更改我的花漾账号
    @Multipart
    @PUT(LiveChatUrl.updateMyHeader)
    suspend fun updateMyHeader(@Part file: MultipartBody.Part):Result<Any>

    //补全信息
    @Multipart
    @POST(LiveChatUrl.completeInformation)
    suspend fun completeInformation(@Part parts:List<MultipartBody.Part>):Result<Any>

    //获取直播间信息无分享人的
    @GET(LiveChatUrl.getLiveRoomDataByLiveId)
    suspend fun getLiveRoomDataByIdAndShare(@Query("id") id:Int,@Query("user_id") user_id:Int) : Result<LiveRoomBean>

    @GET(LiveChatUrl.getLiveRoomDataByLiveId)
    suspend fun getLiveRoomDataByIdNoShare(@Query("id") id:Int) : Result<LiveRoomBean>



}