package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yb.livechatkt.LiveChatKtApplication.Companion.context
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.Row

class HomeLiveRecyclerAdapter(context: Context,val dataList : List<Row>) : RecyclerView.Adapter<HomeLiveRecyclerAdapter.HomeLiveViewHolder>() {


    interface OnItemClickListener{
        fun itemClick(view: View,row: Row,position: Int)
    }

    var onItemClickListener:OnItemClickListener? = null


    class HomeLiveViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var videoTitle:TextView = itemView.findViewById(R.id.videoTitle)
        var videoCover:ImageView = itemView.findViewById(R.id.videoCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeLiveViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.adapter_home_live_item_layout,parent,false)
        return HomeLiveViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: HomeLiveViewHolder, position: Int) {
        holder.videoTitle.text = dataList[position].name
        Glide.with(context).load(dataList[position].cover_img).into(holder.videoCover)
        holder.itemView.setOnClickListener {
            onItemClickListener?.itemClick(holder.itemView,dataList[position],position)
        }
    }

}