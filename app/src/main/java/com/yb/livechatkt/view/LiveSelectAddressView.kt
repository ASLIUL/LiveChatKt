package com.yb.livechatkt.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.AddressBean
import com.yb.livechatkt.databinding.LiveSelectAddressViewBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class LiveSelectAddressView(context: Context, attributeSet: AttributeSet) : RelativeLayout(
    context,
    attributeSet
) {

    private var addressList:MutableList<AddressBean> = ArrayList()
    private var adapter:ArrayAdapter<String>? = null
    private var provinceId:Int = 0
    private var cityId:Int = 0
    private var countyId:Int = 0
    private var dataList:MutableList<String> = ArrayList()

    private var binding:LiveSelectAddressViewBinding
    //1 选择省 2 选择市 3 选择县
    private var currentAction = 1
    private var provincePosition:Int = 0
    private var cityPosition:Int = 0

    private var provinceName:String = ""
    private var cityName:String = ""
    private var countyName:String = ""

    var finishListener:OnSelectFinishListener? = null


    private val TAG = "LiveSelectAddressView"

    init {
        loadData()
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.live_select_address_view,
            this,
            false
        )
        addView(binding.root)
        showView()
    }

    fun showView(){
        if (addressList?.isEmpty()!!){
            return
        }
        dataList.clear()
        addressList.forEach {
            dataList.add(it.name!!)
        }
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, dataList)
        binding.dataList.adapter = adapter
        adapter?.notifyDataSetChanged()
        binding.close.setOnClickListener { visibility = GONE }
        binding.dataList.setOnItemClickListener{ parent, view, position, id ->

            when(currentAction){
                1 ->{
                    provinceName = dataList[position]
                    binding.province.text = dataList[position]
                    provincePosition = position
                    provinceId = addressList[position].id
                    dataList.clear()
                    addressList?.get(position).cityList.forEach{
                        dataList.add(it.name!!)
                    }
                    currentAction = 2
                    adapter?.notifyDataSetChanged()
                }
                2 -> {
                    currentAction = 3
                    binding.city.text = dataList[position]
                    cityName = dataList[position]
                    cityId = addressList[provincePosition]?.cityList[position]?.id
                    cityPosition = position
                    dataList.clear()
                    addressList[provincePosition]?.cityList[position]?.cityList.forEach{
                        dataList.add(it.name!!)
                    }
                    if (dataList.isNotEmpty()){
                        adapter?.notifyDataSetChanged()
                    }else{
                        finishListener?.selectFinish(provinceId,cityId,0,provinceName,cityName,"")
                    }

                }
                3 -> {
                    countyName = dataList[position]
                    countyId = addressList[provincePosition]?.cityList[cityPosition]?.cityList[position]?.id
                    finishListener?.selectFinish(provinceId,cityId,countyId,provinceName,cityName,countyName)
                }
            }


        }

    }

    private fun loadData(){
        var json = ""
        val stringBuilder = StringBuilder()
        try {
            val bf = BufferedReader(InputStreamReader(context.assets.open("city.json")))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            json = stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val type = object : TypeToken<List<AddressBean>>() {}.type
        addressList = Gson().fromJson(json, type)
    }

    interface OnSelectFinishListener{
        fun selectFinish(provinceId:Int,cityId:Int,countyId:Int,provinceName:String,cityName:String,countyName:String)
    }




}