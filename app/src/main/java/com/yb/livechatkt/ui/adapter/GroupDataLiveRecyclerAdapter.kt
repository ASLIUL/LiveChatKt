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

class GroupDataLiveRecyclerAdapter(var context: Context,var dataList:List<ServiceGroup>) : RecyclerView.Adapter<GroupDataLiveRecyclerAdapter.GroupDataViewHolder>(){

    private var chooseGroup:MutableMap<String,ServiceGroup> = HashMap()
    init {
        chooseGroup.clear()
    }


    class GroupDataViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        var groupName:TextView = itemView.findViewById(R.id.group_name)
        var groupHeader:ImageView = itemView.findViewById(R.id.group_header)
        var choose:ImageView = itemView.findViewById(R.id.choose)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupDataViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_group_data_list_layout,parent,false)
        return GroupDataViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: GroupDataViewHolder, position: Int) {
        holder.groupName.text = dataList[position].tname
        Glide.with(context).load(R.mipmap.group_header).into(holder.groupHeader)
        Glide.with(context).load(R.drawable.ic_not_checked_blue).into(holder.choose)

        holder.itemView.setOnClickListener {
            if (chooseGroup.contains(dataList[position].tid)){
                Glide.with(context).load(R.drawable.ic_not_checked_blue).into(holder.choose)
                chooseGroup.remove(dataList[position].tid)
            }else{
                Glide.with(context).load(R.drawable.ic_checked_blue).into(holder.choose)
                chooseGroup[dataList[position].tid] = dataList[position]
            }
        }
    }

    fun getChooseGroup() : MutableMap<String, ServiceGroup> = chooseGroup

}