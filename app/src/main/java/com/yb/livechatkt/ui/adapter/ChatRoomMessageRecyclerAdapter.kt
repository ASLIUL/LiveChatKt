package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.ChatRoomCustomMsg
import java.lang.Exception

class ChatRoomMessageRecyclerAdapter(val context: Context,val dataList:MutableList<ChatRoomCustomMsg>) : RecyclerView.Adapter<ChatRoomMessageRecyclerAdapter.ChatRoomMessageViewHolder>() {


    class ChatRoomMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name:TextView = itemView.findViewById(R.id.name)
        val connect:TextView = itemView.findViewById(R.id.connect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomMessageViewHolder {
        val view  = LayoutInflater.from(context).inflate(R.layout.adapter_chat_room_message_layout,parent,false)
        return ChatRoomMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomMessageViewHolder, position: Int) {
        try {
            holder.name.text = dataList[position].data.username+""
            holder.connect.text = dataList[position].data.connect
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = dataList.size

}