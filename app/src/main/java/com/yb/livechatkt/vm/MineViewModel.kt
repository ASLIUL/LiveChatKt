package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yb.livechatkt.bean.Me
import com.yb.livechatkt.bean.ServicePerson
import com.yb.livechatkt.db.DBManager
import com.yb.livechatkt.util.SaveUserData
import kotlinx.coroutines.launch

class MineViewModel(application: Application) : BaseViewModel(application) {

    var meDataLiveData = MutableLiveData<Me>()
    var isShowService = MutableLiveData<Boolean>()
    var exitLiveData = MutableLiveData<Boolean>()
    var updateStatusLiveData = MutableLiveData<Boolean>()
    var isShowAdmin = MutableLiveData<Boolean>()
    private val sessionDao = DBManager.getDBInstance().getSessionDao()
    private val friendsDao = DBManager.getDBInstance().getFriendsDao()

    init {
        isShowAdmin.value = SaveUserData.get().role == 4
        when(SaveUserData.get().role){
            1,2 -> {
                isShowService.value = true
            }
            3,4 ->{
                isShowService.value = false
            }
        }
    }

    fun getMineData(){
        launch({userApi.getMineData()},meDataLiveData)
    }


    fun serviceState(status:String){
        launchAny({userApi.updateServiceState(status)},updateStatusLiveData)
    }


    fun exitLogin(){
        Log.d(TAG, "exitLogin: 退出登录")
        viewModelScope.launch {
            sessionDao.deleteAllData()
            friendsDao.deleteAllData()
        }
        launchAny({userApi.exitLogin()},exitLiveData)
    }

}