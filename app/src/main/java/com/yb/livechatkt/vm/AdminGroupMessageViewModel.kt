package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.yb.livechatkt.bean.GroupMessageBean
import com.yb.livechatkt.bean.LiveEnum
import com.yb.livechatkt.net.Result
import com.yb.livechatkt.net.RetrofitUtil
import com.yb.livechatkt.util.NetConstant
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AdminGroupMessageViewModel(application: Application) : BaseViewModel(application) {

    var messageStatueLiveData = MutableLiveData<MutableMap<String,LiveEnum>>()
    var fileUpLoadLiveData = MutableLiveData<JsonObject>()
    init {
        messageStatueLiveData.value = HashMap()
    }

    fun sendMessage(bean: GroupMessageBean){
        Log.d(TAG, "sendMessage: 正在发送消息")

        messageStatueLiveData.value?.put(bean.uuid,LiveEnum.sending)

        launchHashMap({userApi.adminSendMessage(RetrofitUtil.get(getApplication()).createJsonRequest(bean))},messageStatueLiveData,bean.uuid)

    }

    fun uploadFileCreteMessage(bean: GroupMessageBean){

        var file:File = File(bean.filepPath)
        Log.d(TAG, "uploadFileCreteMessage: ${file.absolutePath}")
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val body = RequestBody.create("multipart/form-data".toMediaType(), file)
        builder.addFormDataPart("file",file.name,body)
        builder.addFormDataPart("type",bean.type.toString())

        val parts = builder.build().parts

        viewModelScope.launch {
            val result:Result<Any> = userApi.adminUploadMessageFile(parts)
            Log.d(TAG, "uploadFileCreteMessage: ${result.code} \t ${result.msg}")
            if (result.code == NetConstant.responseSuccessCode){
                bean.file = result.msg
                sendMessage(bean)
            }
        }

    }
}