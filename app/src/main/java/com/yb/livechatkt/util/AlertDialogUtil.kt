package com.yb.livechatkt.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.DialogSelectAddressLayoutBinding
import com.yb.livechatkt.databinding.DialogShowGiftDataLayoutBinding
import com.yb.livechatkt.view.LiveSelectAddressView

var addressLiveData = MutableLiveData<MutableMap<String,Any>>()
fun selectAddressDialog(context: Context){
    val builder = AlertDialog.Builder(context,R.style.dialog)
    val binding:DialogSelectAddressLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
        R.layout.dialog_select_address_layout,null,false)
    builder.setView(binding.root)
    val dialog = builder.create()
    binding.addressView.setOnSelectFinishListener{ provinceId, cityId, countyId, provinceName, cityName, countyName ->
            val hashMap:MutableMap<String,Any> = HashMap()
            hashMap[NetConstant.PROVINCE_ID] = provinceId
            hashMap[NetConstant.CITY_ID] = cityId
            hashMap[NetConstant.COUNTY_ID] = countyId
            hashMap[NetConstant.PROVINCE_NAME] = provinceName
            hashMap[NetConstant.CITY_NAME] = cityName
            hashMap[NetConstant.COUNTY_NAME] = countyName
            addressLiveData.value = hashMap
            dialog.dismiss()

    }
    val alertDialog = builder.create()
    alertDialog.show()
    var layoutParams = alertDialog.window?.attributes
    layoutParams?.width = getScreenWidth()
    layoutParams?.height = (getScreenHeight() /3)*2
    layoutParams?.gravity = Gravity.BOTTOM
    layoutParams?.dimAmount = 0.6f
    layoutParams?.windowAnimations = R.style.dialog_enter_exit
    alertDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    alertDialog.window?.attributes = layoutParams
}

fun showGiftDataDialog(context: Context){
    val binding:DialogShowGiftDataLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.dialog_show_gift_data_layout,null,false)
    val builder = AlertDialog.Builder(context)
    builder.setView(binding.root)
    

}




















