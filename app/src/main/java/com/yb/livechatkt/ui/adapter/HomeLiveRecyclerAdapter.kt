package com.yb.livechatkt.ui.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.yb.livechatkt.bean.HomeLiveBean
import com.yb.livechatkt.bean.Row
import com.yb.livechatkt.databinding.AdapterHomeLiveItemLayoutBinding

class HomeLiveRecyclerAdapter(context: Context,dataList : List<Row>) :  BaseRecyclerAdapter<AdapterHomeLiveItemLayoutBinding,Row>(context,dataList) {
    override fun initView(holder: BaseViewHolder, t: Row, position: Int) {
        val binding = holder.binding as AdapterHomeLiveItemLayoutBinding
        binding.videoTitle.text = t.name
        Glide.with(context).load(t.cover_img).into(binding.videoCover)
    }

}