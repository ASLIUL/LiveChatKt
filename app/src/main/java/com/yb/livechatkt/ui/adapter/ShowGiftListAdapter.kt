package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.AdapterShowGiftItemLayoutBinding

class ShowGiftListAdapter(private val context: Context,private val dataList:List<Any>):RecyclerView.Adapter<ShowGiftListAdapter.GiftViewHolder>() {

    class GiftViewHolder(val binding: AdapterShowGiftItemLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        val binding = DataBindingUtil.inflate<AdapterShowGiftItemLayoutBinding>(LayoutInflater.from(context),
            R.layout.adapter_show_gift_item_layout,parent,false)
        return GiftViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        val binding = holder.binding

    }

    override fun getItemCount(): Int = dataList.size


}