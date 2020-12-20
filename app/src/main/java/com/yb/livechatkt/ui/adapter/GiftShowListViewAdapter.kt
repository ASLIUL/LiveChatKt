package com.yb.livechatkt.ui.adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.RoomGiftMessage
import com.yb.livechatkt.bean.RoomTextMessage


/**
 * create by liu
 * on 2020/6/8 3:08 PM
 */

class GiftShowListViewAdapter(private val context: Context, private val giftBeans: List<RoomGiftMessage>) : RecyclerView.Adapter<GiftShowListViewAdapter.ListViewHolder>(){

    class ListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val imageView:ImageView = itemView.findViewById(R.id.gift_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_gift_show_list_view_layout, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(context).load(giftBeans[position].giftimg).into(holder.imageView)
    }

    override fun getItemCount(): Int = giftBeans.size

}