package com.yb.livechatkt.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yb.livechatkt.util.NetConstant
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import kotlin.math.sign

class CompleteInformationViewModel(application: Application) :BaseViewModel(application) {

    var updateDataLiveData = MutableLiveData<Boolean>()

    fun submitMyData(hashMap: MutableMap<String,Any>){
        var file = File(hashMap["header"].toString())
        if (!file.exists() || file.isDirectory){
            return
        }
        var builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        var body = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),file)
        builder.addFormDataPart("file",file.name,body)
        builder.addFormDataPart("area_id",hashMap[NetConstant.COUNTY_ID].toString())
        builder.addFormDataPart("city_id",hashMap[NetConstant.CITY_ID].toString())
        builder.addFormDataPart("province_id",hashMap[NetConstant.PROVINCE_ID].toString())
        builder.addFormDataPart("sex",hashMap["sex"].toString())
        builder.addFormDataPart("signature",hashMap["sign"].toString())
        builder.addFormDataPart("username",hashMap["username"].toString())
        builder.addFormDataPart("address",hashMap["address"].toString())
        val parts = builder.build().parts
        launchAny({userApi.completeInformation(parts)},updateDataLiveData)

    }

}