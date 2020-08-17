package com.yb.livechatkt.vm

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.yb.livechatkt.LiveChatKtApplication
import com.yb.livechatkt.bean.ErrorMessageBean
import com.yb.livechatkt.bean.GroupMessageBean
import com.yb.livechatkt.bean.LiveEnum
import com.yb.livechatkt.net.Result
import com.yb.livechatkt.net.RetrofitUtil
import com.yb.livechatkt.net.UserApi
import com.yb.livechatkt.ui.activity.LoginActivity
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

open class BaseViewModel(application: Application) : AndroidViewModel(application) {


    init {
        var receiveMessageLiveData = MutableLiveData<List<IMMessage>>()
        NIMClient.getService(MsgServiceObserve::class.java).observeReceiveMessage({
                receiveMessageLiveData.value = it
            }, true)
    }

    val TAG = this.javaClass.simpleName

    val userApi = RetrofitUtil.get(application).createService(UserApi::class.java)

    var tokenFailed = MutableLiveData<Boolean>()

    var isShowLoading = MutableLiveData<Boolean>()
    var isShowError = MutableLiveData<ErrorMessageBean>()
    var isSuccessNoT = MutableLiveData<Boolean>()

    fun showLoading(){
        isShowLoading.value =  true
    }
    fun dismissLoading(){
        isShowLoading.value = false;
    }
    fun showError(error:ErrorMessageBean){
        isShowError.value = error
    }
    fun tokenError(){
        Log.d(TAG, "tokenError: 啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊")
        tokenFailed.value = true
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
        viewModelScope.launch {
            try {
                var result: Result<T>? = null
                withContext(Dispatchers.IO) {
                    result = block()
                }
                if (result!!.code == NetConstant.responseSuccessCode) {//请求成功
                    liveData.value = result!!.data
                } else if (result!!.code == NetConstant.responseTokenFailedCode) {
                    Log.d(TAG, "launch: token失效")
                    tokenError()
                }else{
                    Log.d(TAG, "launch: "+result?.code!!+"\t"+result?.msg!!)
                    showError(ErrorMessageBean(result?.code!!,result?.msg!!))
                }
            } catch (e: Throwable) {//接口请求失败
                Log.d(TAG, "launch: 失败"+e.message)
                showError(ErrorMessageBean(NetConstant.responseErrorCode,NetConstant.reponseErrorMsg))
            } finally {//请求结束
                dismissLoading()
            }
        }
    }

    fun launchNoT(
        block : suspend CoroutineScope.() -> Result<Any>,
        isShowLoading: Boolean = false,
        isShowError: Boolean = false
    ){
        if (isShowLoading) showLoading()
        viewModelScope.launch {
            try {
                var result:Result<Any>? = null
                withContext(Dispatchers.IO){
                    result = block()
                }
                if (result!!.code == NetConstant.responseSuccessCode){
                    isSuccessNoT.value = true
                }else {
                    isSuccessNoT.value = false
                    showError(ErrorMessageBean(result!!.code,result!!.msg))
                }
            }catch (e:Throwable){
                isSuccessNoT.value = false
            }finally {
                dismissLoading()
            }
        }
    }
    fun launchHashMap(
        block : suspend CoroutineScope.() -> Result<Any>,
        liveData: MutableLiveData<MutableMap<String,LiveEnum>>,
        key:String
    ){
        var hashMap:MutableMap<String,LiveEnum> = HashMap()
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
            }catch (e:Throwable){
                hashMap[key] = LiveEnum.error
                liveData.postValue(hashMap)
                Log.d(TAG, "E launchHashMap: ${liveData.value.toString()}")
            }finally {
            }
        }
    }

}