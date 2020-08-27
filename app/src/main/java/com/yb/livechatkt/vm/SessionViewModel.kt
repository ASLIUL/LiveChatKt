package com.yb.livechatkt.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallbackWrapper
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.yb.livechatkt.bean.Session
import com.yb.livechatkt.db.DBManager
import com.yb.livechatkt.util.NetConstant
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) : BaseViewModel(application) {

    private val sessionDao = DBManager.getDBInstance().getSessionDao()
    val historyMessageLiveData = MutableLiveData<List<RecentContact>>()

    fun updateSession(session: Session){
        viewModelScope.launch {
            sessionDao.updateSession(session.accid, session.lastMsg, session.lastMsgTime)
        }
    }
    fun insertSession(session: Session){
        viewModelScope.launch {
            var s = sessionDao.selectOneSessionByAccId(session.accid)
            if (s!=null){
                sessionDao.updateSession(session.accid,session.lastMsg,session.lastMsgTime)
            }else{
                sessionDao.insertOneSession(session)
            }
        }
    }
    fun deleteSession(session: Session){
        viewModelScope.launch {
            //sessionDao.deleteOneSession(accid)
            sessionDao.deleteSession(session)
        }
    }
    fun getHistorySession(){
        NIMClient.getService(MsgService::class.java).queryRecentContacts().setCallback(object :
            RequestCallbackWrapper<List<RecentContact>>() {
            override fun onResult(code: Int, result: List<RecentContact>?, exception: Throwable?) {
                if (code == NetConstant.responseSuccessCode && !result.isNullOrEmpty()) {
                    historyMessageLiveData.value = result
                }
            }
        })
    }


}