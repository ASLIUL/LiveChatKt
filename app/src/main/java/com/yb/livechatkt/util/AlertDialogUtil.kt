package com.yb.livechatkt.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.DialogSelectAddressLayoutBinding
import com.yb.livechatkt.view.LiveSelectAddressView

object AlertDialogUtil {

    var addressLiveData = MutableLiveData<MutableMap<String,Any>>()

    fun selectAddressDialog(context: Context):AlertDialog{
        val builder = AlertDialog.Builder(context,R.style.dialog)
        val binding:DialogSelectAddressLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.dialog_select_address_layout,null,false)
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.addressView.finishListener = object : LiveSelectAddressView.OnSelectFinishListener{
            override fun selectFinish(
                provinceId: Int,
                cityId: Int,
                countyId: Int,
                provinceName: String,
                cityName: String,
                countyName: String
            ) {
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

        }
        return dialog
    }
}
/*
 val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER
        Glide.with(context).load(url).placeholder(R.mipmap.group_header).into(imageView)
        val imageParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = imageParams
 */