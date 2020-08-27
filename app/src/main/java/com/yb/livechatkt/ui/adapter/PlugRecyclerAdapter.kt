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
import com.yb.livechatkt.bean.PlugBean

class PlugRecyclerAdapter(val context: Context,val dataList:MutableList<PlugBean>) : RecyclerView.Adapter<PlugRecyclerAdapter.PlugViewHolder>() {

    interface OnItemClickListener{
        fun itemClick(view: View,plugBean: PlugBean,position: Int)
    }

    var onItemClickListener:OnItemClickListener? = null


    class PlugViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val actionImg:ImageView = itemView.findViewById(R.id.action_image)
        val actionName:TextView = itemView.findViewById(R.id.action_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlugViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_plug_in_unit_layout,parent,false)
        return PlugViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlugViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].action.resId).into(holder.actionImg)
        holder.actionName.text = context.resources.getString(dataList[position].action.actionName)
        holder.itemView.setOnClickListener {
            onItemClickListener?.itemClick(holder.itemView,dataList[position],position)
        }
    }

    override fun getItemCount(): Int = dataList.size
}