package com.yb.livechatkt.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yb.livechatkt.bean.ServiceGroup

class ChooseGroupViewModel(application: Application) : BaseViewModel(application) {

    var groupDataLiveData = MutableLiveData<List<ServiceGroup>>()

    fun getAllGroup(){
        launch({userApi.getAllGroup()},groupDataLiveData)
    }

}