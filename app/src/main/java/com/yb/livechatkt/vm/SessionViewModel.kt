package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.RecentContact

class SessionViewModel(application: Application) : BaseViewModel(application) {

    val historyRecentContactLiveData = MutableLiveData<List<RecentContact>>()

    //获取最近会话
    fun getRecentContacts(){
        NIMClient.getService(MsgService::class.java).queryRecentContacts().setCallback(object :
            RequestCallback<List<RecentContact>> {
            override fun onSuccess(param: List<RecentContact>?) {
                if (!param.isNullOrEmpty()){
                    historyRecentContactLiveData.value = param
                }
            }
            override fun onFailed(code: Int) {
                Log.d(TAG, "onFailed: $code")
            }

            override fun onException(exception: Throwable?) {
                Log.d(TAG, "onException: ${exception?.message}")
            }
        })
    }
    //删除最近会话
    fun deleteRecentContacts(recentContact: RecentContact){
        Log.d(TAG, "deleteRecentContacts: 测试删除")
        NIMClient.getService(MsgService::class.java).deleteRecentContact2(recentContact.contactId,recentContact.sessionType)
    }



}