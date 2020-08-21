package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yb.livechatkt.bean.Contacts
import com.yb.livechatkt.bean.Friends
import com.yb.livechatkt.bean.RowX
import com.yb.livechatkt.db.DBManager
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.util.SaveUserData
import kotlinx.coroutines.launch

class ContactsViewModel(application: Application) : BaseViewModel(application) {


    var contactsLiveData = MutableLiveData<Contacts>()

    fun getContacts(){
        launch({userApi.getContacts(1,100)},contactsLiveData)
    }

    fun saveDataToDB(){
        if (!SaveUserData.get().isNeedUpdateDB) return
        val friendsDao = DBManager.getDBInstance().getFriendsDao()
        var rowXData:MutableList<RowX> = ArrayList()
        rowXData.clear()
        contactsLiveData.value?.page?.rows?.let { rowXData.addAll(it) }
        if (rowXData.isEmpty()){
            return
        }

        var friendsData:MutableList<Friends> = ArrayList()
        rowXData.forEach {
            var friends = Friends(it.accid,LiveChatUrl.imgBaseUrl+it.accid,it.id,it.name,it.sex,it.signature)
            friendsData.add(friends)
        }
        viewModelScope.launch {
            friendsDao.insertFriendsData(friendsData)
        }
        SaveUserData.get().isNeedUpdateDB = false

    }

}