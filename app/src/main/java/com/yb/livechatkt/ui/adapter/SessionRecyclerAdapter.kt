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
import com.yb.livechatkt.bean.Session

class SessionRecyclerAdapter(val context: Context,val dataList:List<Session>) : RecyclerView.Adapter<SessionRecyclerAdapter.SessionViewHolder>() {

    class SessionViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var name:TextView = itemView.findViewById(R.id.name)
        var time:TextView = itemView.findViewById(R.id.time)
        var msg:TextView = itemView.findViewById(R.id.msg)
        var header:ImageView = itemView.findViewById(R.id.header)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_session_item_layout,parent,false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.name.text = dataList[position].name
        holder.time.text = dataList[position].lastMsgTime.toString()
        holder.msg.text = dataList[position].lastMsg
        Glide.with(context).load(dataList[position].header).into(holder.header)
    }

    override fun getItemCount(): Int = dataList.size

}