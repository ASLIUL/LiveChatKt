package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.GiftItemData
import com.yb.livechatkt.bean.GiftTitleTab
import com.yb.livechatkt.databinding.AdapterShowGiftItemLayoutBinding

class ShowGiftListAdapter(private val context: Context,private val dataList:List<GiftItemData>?):RecyclerView.Adapter<ShowGiftListAdapter.GiftViewHolder>() {

    class GiftViewHolder(val binding: AdapterShowGiftItemLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        val binding = DataBindingUtil.inflate<AdapterShowGiftItemLayoutBinding>(LayoutInflater.from(context),
            R.layout.adapter_show_gift_item_layout,parent,false)
        return GiftViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        val binding = holder.binding
        if (dataList.isNullOrEmpty()) return
        Glide.with(context).asGif().load(dataList[position].icon).into(binding.giftImg)
        binding.giftName.text = dataList[position].name
        binding.giftPrice.text = "¥${dataList[position].price}"

        if (dataList[position].check == 1){
            binding.giftImg.setBorderWidth(1)
            binding.giftImg.setBorderColor(context.resources.getColor(R.color.theme_bg_color))
        }else{
            binding.giftImg.setBorderWidth(0)
        }



        binding.root.setOnClickListener {
            onItemClick(dataList[position])
        }

    }

    override fun getItemCount(): Int = if (dataList.isNullOrEmpty()) 0 else dataList?.size


    private var onItemClick:(GiftItemData) ->Unit = {}

    fun setOnItemClickListener(onItemClick:(GiftItemData) ->Unit){
        this.onItemClick = onItemClick
    }


}