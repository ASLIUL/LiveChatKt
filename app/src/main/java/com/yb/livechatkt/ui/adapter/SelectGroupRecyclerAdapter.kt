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
import com.yb.livechatkt.bean.ServiceGroup
import kotlinx.android.synthetic.main.adapter_select_group_list_layout.view.*

class SelectGroupRecyclerAdapter(val context: Context,val dataList:MutableList<ServiceGroup>) : RecyclerView.Adapter<SelectGroupRecyclerAdapter.GroupViewHolder>() {


    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val header:ImageView = itemView.findViewById(R.id.header)
        val name:TextView = itemView.findViewById(R.id.group_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_select_group_list_layout,parent,false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.name.text = dataList[position].tname
        Glide.with(context).load(R.mipmap.group_header).into(holder.header)
    }

    override fun getItemCount(): Int = dataList.size

}