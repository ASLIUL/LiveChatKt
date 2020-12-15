package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.yb.livechatkt.bean.ErrorMessageBean
import com.yb.livechatkt.bean.LiveEnum
import com.yb.livechatkt.net.Result
import com.yb.livechatkt.net.RetrofitUtil
import com.yb.livechatkt.net.UserApi
import com.yb.livechatkt.ui.activity.HomeMainActivity
import com.yb.livechatkt.util.NetConstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

     val BaseTAG = "BaseViewModel"

    var receiveMessageLiveData = MutableLiveData<List<IMMessage>>()
    var isStartMonitorMessage = MutableLiveData<Boolean>()
    //登陆状态监听
    var wyLoginMonitor = MutableLiveData<Boolean>()
    var userNotDataLiveData = MutableLiveData<Boolean>()
    //未读计数
    val unReadNum = MutableLiveData<Int>()
    //最近会话列表数据
    val recentContactLiveData = MutableLiveData<List<RecentContact>>()
    val deleteRecentContactLiveData = MutableLiveData<RecentContact>()
    val isOffLineLiveData = MutableLiveData<Boolean>()



    init {
        userNotDataLiveData.value = false
        isStartMonitorMessage.value = true
        wyLoginMonitor.value = true
        NIMClient.getService(MsgServiceObserve::class.java).observeReceiveMessage({
            receiveMessageLiveData.value = it
        }, isStartMonitorMessage.value!!)

        unReadNum.value = NIMClient.getService(MsgService::class.java).totalUnreadCount
        NIMClient.getService(MsgServiceObserve::class.java).observeRecentContact({
            recentContactLiveData.value = it
        }, true)
        NIMClient.getService(MsgServiceObserve::class.java).observeRecentContactDeleted({
            deleteRecentContactLiveData.value = it
        },true)
        isOffLineLiveData.value = false
    }

    val TAG = this.javaClass.simpleName

    val userApi = RetrofitUtil.get(application).createService(UserApi::class.java)

    var tokenFailed = MutableLiveData<Boolean>()

    var isShowLoading = MutableLiveData<Boolean>()
    var isShowError = MutableLiveData<ErrorMessageBean>()

    private fun showLoading(){
        isShowLoading.value =  true
    }
    private fun dismissLoading(){
        isShowLoading.value = false;
    }
    fun showError(error: ErrorMessageBean){
        isShowError.value = error
    }
    fun tokenError(){
        tokenFailed.value = true
    }
    fun isMonitorWyLogin(isObserve: Boolean){
        NIMClient.getService(AuthServiceObserver::class.java).observeOnlineStatus({
            //if (it.wontAutoLogin()) isOffLineLiveData.value = true
            if (it == StatusCode.LOGINED) wyLoginMonitor.value = true
            if (it == StatusCode.UNLOGIN || it == StatusCode.PWD_ERROR || it == StatusCode.VER_ERROR) wyLoginMonitor.value =
                false
        }, isObserve)
    }
    fun monitorRecentContacts(isStart: Boolean){
        NIMClient.getService(MsgServiceObserve::class.java).observeRecentContact({
                recentContactLiveData.value = it
            }, isStart)
    }




    /**
     * 请求接口，可定制是否显示loading和错误提示
     */
    fun <T> launch(
        block: suspend CoroutineScope.() -> Result<T>,//请求接口方法，T表示data实体泛型，调用时可将data对应的bean传入即可
        liveData: MutableLiveData<T>,
        isShowLoading: Boolean = false,
        isShowError: Boolean = true
    ) {
        if (isShowLoading) showLoading()
        viewModelScope.launch { HomeMainActivity::class.java
            try {
                var result: Result<T>? = null
                withContext(Dispatchers.IO) {
                    result = block()
                }
                Log.d(TAG, "launch: ${result!!.msg} \t ${result!!.code}")
                when (result!!.code) {
                    NetConstant.responseSuccessCode -> {//请求成功
                        liveData.value = result!!.data
                        Log.d(TAG, "launch: ${result!!.data}")
                    }
                    NetConstant.responseTokenFailedCode -> {
                        tokenError()
                    }
                    NetConstant.responseNoDataCode -> {
                        userNotDataLiveData.value = true
                    }
                    else -> {
                        showError(ErrorMessageBean(result?.code!!, result?.msg!!))
                    }
                }
            } catch (e: Throwable) {//接口请求失败
                e.printStackTrace()
                showError(
                    ErrorMessageBean(
                        NetConstant.responseErrorCode,
                        NetConstant.reponseErrorMsg
                    )
                )
            } finally {//请求结束
                dismissLoading()
            }
        }
    }

    fun launchAny(
        block: suspend CoroutineScope.() -> Result<Any>,
        liveData: MutableLiveData<Boolean>
    ){
        viewModelScope.launch {
            try {
                var result:Result<Any>? = null
                withContext(Dispatchers.IO){
                    result = block()
                }
                if (result!!.code == NetConstant.responseSuccessCode){
                    liveData.value = true
                }else if (result!!.code == NetConstant.responseTokenFailedCode){
                    liveData.value = false
                    tokenError()
                }else{
                    liveData.value = false
                    showError(ErrorMessageBean(result!!.code, result!!.msg))
                }
            }catch (e: Throwable){
                liveData.value = false
            }finally {
                dismissLoading()
            }
        }
    }
    fun launchHashMap(
        block: suspend CoroutineScope.() -> Result<Any>,
        liveData: MutableLiveData<MutableMap<String, LiveEnum>>,
        key: String
    ){
        var hashMap:MutableMap<String, LiveEnum> = HashMap()
        viewModelScope.launch {
            try {
                var result:Result<Any>? = null
                withContext(Dispatchers.IO){
                    result = block()
                }
                if (result!!.code == NetConstant.responseSuccessCode){
                    hashMap[key] = LiveEnum.success
                    liveData.postValue(hashMap)
                    Log.d(TAG, "S launchHashMap: ${liveData.value.toString()}")
                }else {
                    hashMap[key] = LiveEnum.error
                    liveData.postValue(hashMap)
                    Log.d(TAG, "L launchHashMap: ${liveData.value.toString()}")
                }
            }catch (e: Throwable){
                hashMap[key] = LiveEnum.error
                liveData.postValue(hashMap)
                Log.d(TAG, "E launchHashMap: ${liveData.value.toString()}")
            }finally {
            }
        }
    }
    fun launchKey(
        block: suspend CoroutineScope.() -> Result<Any>,
        liveData: MutableLiveData<MutableMap<String, LiveEnum>>,
        key: String
    ){

        var hashMap:MutableMap<String, LiveEnum> = HashMap()
        hashMap[key] = LiveEnum.sending
        viewModelScope.launch {
            try {
                var result:Result<Any>? = null
                withContext(Dispatchers.IO){
                    result = block()
                }
                if (result?.code == NetConstant.responseSuccessCode){
                    hashMap[key] = LiveEnum.success
                }else if (result?.code == NetConstant.responseTokenFailedCode){
                    hashMap[key]= LiveEnum.error
                    tokenError()
                }else{
                    hashMap[key]= LiveEnum.error
                }
                liveData.postValue(hashMap)
            }catch (e: Throwable){
            }
        }


    }

}