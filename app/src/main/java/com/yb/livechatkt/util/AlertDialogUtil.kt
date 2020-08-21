package com.yb.livechatkt.util

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.DialogSelectAddressLayoutBinding
import com.yb.livechatkt.view.LiveSelectAddressView

object AlertDialogUtil {

    var addressLiveData = MutableLiveData<MutableMap<String,Any>>()

    fun selectAddressDialog(context: Context):AlertDialog{
        var builder = AlertDialog.Builder(context,R.style.dialog)
        var binding:DialogSelectAddressLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.dialog_select_address_layout,null,false)
        builder.setView(binding.root)
        var dialog = builder.create()
        binding.addressView.finishListener = object : LiveSelectAddressView.OnSelectFinishListener{
            override fun selectFinish(
                provinceId: Int,
                cityId: Int,
                countyId: Int,
                provinceName: String,
                cityName: String,
                countyName: String
            ) {
                var hashMap:MutableMap<String,Any> = HashMap()
                hashMap[NetConstant.PROVINCE_ID] = provinceId
                hashMap[NetConstant.CITY_ID] = cityId
                hashMap[NetConstant.COUNTY_ID] = countyId
                hashMap[NetConstant.PROVINCE_NAME] = provinceName
                hashMap[NetConstant.CITY_NAME] = cityName
                hashMap[NetConstant.COUNTY_NAME] = countyName
                addressLiveData.value = hashMap
                dialog.dismiss()
            }

        }
        return dialog
    }
}