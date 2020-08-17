package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yb.livechatkt.bean.Contacts

class ContactsViewModel(application: Application) : BaseViewModel(application) {


    var contactsLiveData = MutableLiveData<Contacts>()

    fun getContacts(){
        Log.d(TAG, "getContacts: 开始请求")
        launch({userApi.getContacts(1,100)},contactsLiveData)
    }

}