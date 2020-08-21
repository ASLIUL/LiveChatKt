package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yb.livechatkt.net.RetrofitUtil
import com.yb.livechatkt.util.NetConstant
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import java.io.File

class UpdateUserDataViewModel(application: Application) :BaseViewModel(application) {

    var updateMsgLiveData = MutableLiveData<Boolean>()

    fun updateMyData(dataType:Int,value:String,addressHashMap:MutableMap<String,Any>?){
        val hashMap:MutableMap<String,Any> = HashMap()
        when(dataType){
            1->{
                hashMap.clear()
                hashMap["username"] = value
            }
            2 -> {
                hashMap.clear()
                hashMap["sex"] = value
            }
            3 ->{
                hashMap.clear()
                hashMap["signature"] = value
            }
            4 ->{
                hashMap.clear()
                var address = addressHashMap?.get(NetConstant.PROVINCE_NAME)!!.toString()+" "+ addressHashMap?.get(NetConstant.CITY_NAME)!!.toString()+" "+addressHashMap[NetConstant.COUNTY_NAME].toString()
                address = address.replace("\n","").trim()
                hashMap["address"] = address
                hashMap["area_id"] = addressHashMap[NetConstant.COUNTY_ID].toString()
                hashMap["city_id"] = addressHashMap[NetConstant.CITY_ID].toString()
                hashMap["province_id"] = addressHashMap[NetConstant.PROVINCE_ID].toString()
            }
        }
        var body = RetrofitUtil.get(getApplication()).createJsonRequest(hashMap)
        launchAny({userApi.updateMyData(body)},updateMsgLiveData)
    }
    fun updateMyHyNum(hyNum:String){
        launchAny({userApi.updateMyHyNum(hyNum)},updateMsgLiveData)
    }
    fun updateMyHeader(file: File){
        if (!file.exists() || file.isDirectory){
            return
        }
        Log.d(TAG, "updateMyHeader: ${file.absolutePath}")
        var requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(),file)
        var body = MultipartBody.Part.createFormData("file",file.name,requestBody)
        launchAny({userApi.updateMyHeader(body)},updateMsgLiveData)
    }


}