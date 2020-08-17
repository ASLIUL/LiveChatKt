package com.yb.livechatkt.vm

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import com.yb.livechatkt.bean.LoginDataBean
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.showToast

public class LoginViewModel(application: Application) : BaseViewModel(application) {



    var phone:MutableLiveData<String> = MutableLiveData()

    var password:MutableLiveData<String> = MutableLiveData()

    var loginDataLiveData = MutableLiveData<LoginDataBean>()

    var isLoginSuccess = MutableLiveData<Boolean>()

    fun login(){
        if (TextUtils.isEmpty(phone.value)){
            NetConstant.PLEASRINPUTPHONE.showToast()
            return
        }

        if (TextUtils.isEmpty(password.value)){
            NetConstant.PLEASRINPUTPHONE.showToast()
            return
        }

        var  hashMap = HashMap<String,Any>()
        hashMap.apply {
            put("mobile",phone.value!!)
            put("password",password.value!!)
            put("type",1)

        }
        launch({userApi.login(hashMap)},loginDataLiveData)
    }

    fun wyLogin(accid:String,imToken:String){
        val info = LoginInfo(
            accid,
            imToken,
            NetConstant.IM_APP_KEY
        )
        val loginInfoRequestCallback: RequestCallback<LoginInfo> =
            object : RequestCallback<LoginInfo> {
                override fun onSuccess(param: LoginInfo) {
                    isLoginSuccess.value = true
                }

                override fun onFailed(code: Int) {
                    isLoginSuccess.value = false
                }

                override fun onException(exception: Throwable) {
                    Log.d(TAG, "onException: " + exception.message)
                }
            }
        NIMClient.getService(AuthService::class.java).login(info)
            .setCallback(loginInfoRequestCallback)
    }

}