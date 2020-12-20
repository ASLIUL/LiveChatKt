package com.yb.livechatkt.util

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.GiftData
import com.yb.livechatkt.bean.GiftItemData
import com.yb.livechatkt.bean.GiftTitleTab
import com.yb.livechatkt.databinding.DialogSelectAddressLayoutBinding
import com.yb.livechatkt.databinding.DialogShowGiftDataLayoutBinding
import com.yb.livechatkt.ui.adapter.ShowGiftListAdapter
import com.yb.livechatkt.ui.adapter.ShowGiftTitleAdapter

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
    val layoutParams = alertDialog.window?.attributes
    layoutParams?.width = getScreenWidth()
    layoutParams?.height = (getScreenHeight() /3)*2
    layoutParams?.gravity = Gravity.BOTTOM
    layoutParams?.dimAmount = 0.6f
    layoutParams?.windowAnimations = R.style.dialog_enter_exit
    alertDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    alertDialog.window?.attributes = layoutParams
}

fun showGiftDataDialog(context: Context,balance:Double,titles:List<GiftTitleTab>,giftDataMap:Map<Int,GiftData>,sureListener:(Int,GiftItemData?)->Unit){
    var chooseId = titles[0].id
    var giftDatas = giftDataMap[chooseId]?.gifts
    var selectGiftItemData:GiftItemData? = null
    var selectNum = 1
    val binding:DialogShowGiftDataLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.dialog_show_gift_data_layout,null,false)
    val builder = AlertDialog.Builder(context,R.style.dialog)
    builder.setView(binding.root)
    val alertDialog = builder.create()
    binding.balance.text = String.format(context.resources.getString(R.string.i_have_num_money),balance)
    val titleAdapter = ShowGiftTitleAdapter(context,titles)
    binding.giftTitle.layoutManager = LinearLayoutManager(context).also { it.orientation = RecyclerView.HORIZONTAL }
    binding.giftTitle.adapter = titleAdapter

    Log.d("LivePlayActivity", "showGiftDataDialog: ${giftDatas?.get(0)}")
    val itemAdapter = ShowGiftListAdapter(context,giftDatas)
    binding.giftRecycler.layoutManager = StaggeredGridLayoutManager(5,RecyclerView.VERTICAL)
    binding.giftRecycler.adapter = itemAdapter

    titleAdapter.setOnItemClickListener {
        if (it.check == 1) return@setOnItemClickListener
        titles.forEach {tab ->
            if (tab.id == it.id){
                tab.check = 1
                chooseId = tab.id
                giftDatas = giftDataMap[chooseId]?.gifts
            }else{
                tab.check = 0
            }
        }
        itemAdapter.notifyDataSetChanged()
        titleAdapter.notifyDataSetChanged()
    }

    itemAdapter.setOnItemClickListener {
        selectGiftItemData = it
        if (it.check == 1) return@setOnItemClickListener
        giftDatas?.forEach {gift ->
            if (gift == it){
                gift.check = 1
            }else{
                gift.check = 0
            }
        }
        itemAdapter.notifyDataSetChanged()
    }

    binding.sendGift.setOnClickListener {
        sureListener(selectNum,selectGiftItemData)
        alertDialog.dismiss()
    }
    alertDialog.show()
    val layoutParams = alertDialog.window?.attributes
    layoutParams?.width = getScreenWidth()
    layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
    layoutParams?.gravity = Gravity.BOTTOM
    layoutParams?.dimAmount = 0.2f
    layoutParams?.windowAnimations = R.style.dialog_enter_exit
    alertDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    alertDialog.window?.attributes = layoutParams


}




















