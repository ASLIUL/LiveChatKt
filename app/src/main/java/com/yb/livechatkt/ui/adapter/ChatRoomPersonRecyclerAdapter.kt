package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.ChatRoomPerson

class ChatRoomPersonRecyclerAdapter(val context: Context,val dataList:MutableList<ChatRoomPerson>):RecyclerView.Adapter<ChatRoomPersonRecyclerAdapter.RoomPersonViewHolder>() {

    class RoomPersonViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val header:ImageView = itemView.findViewById(R.id.header)
        val num:TextView = itemView.findViewById(R.id.num)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomPersonViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_room_person_layout,parent,false)
        return RoomPersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomPersonViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].header).into(holder.header)
    }

    override fun getItemCount(): Int = dataList.size
}