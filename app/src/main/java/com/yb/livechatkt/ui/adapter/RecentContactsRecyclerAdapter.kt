package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.UserService
import com.yb.livechatkt.R
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.getConnect
import com.yb.livechatkt.util.transToString


class RecentContactsRecyclerAdapter(val context: Context, val dataList: MutableList<RecentContact>) :RecyclerView.Adapter<RecentContactsRecyclerAdapter.RecentContactsViewHolder>() {

    class RecentContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name: TextView = itemView.findViewById(R.id.name)
        var time: TextView = itemView.findViewById(R.id.time)
        var msg: TextView = itemView.findViewById(R.id.msg)
        var header: ImageView = itemView.findViewById(R.id.header)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentContactsViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.adapter_session_item_layout,
            parent,
            false
        )
        return RecentContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentContactsViewHolder, position: Int) {

        if (dataList[position].sessionType == SessionTypeEnum.Team){
            holder.name.text = context.resources.getString(R.string.hy_service)
            Glide.with(context).load(R.mipmap.group_header).into(
                holder.header
            )
        }else{
            val user = NIMClient.getService(UserService::class.java).getUserInfo(dataList[position].contactId)
            Log.d("TAG", "onBindViewHolder: ${dataList[position].contactId}")
            Log.d("TAG", "onBindViewHolder: ${user?.name}")
            holder.name.text = user?.name
            Glide.with(context).load(LiveChatUrl.headerBaseUrl + dataList[position].contactId).into(
                holder.header
            )
        }

        holder.time.text = transToString(dataList[position].time)
        holder.msg.text = getConnect(dataList[position])

        holder.itemView.setOnClickListener {
            onItemClickListener?.itemClickListener(holder.itemView, dataList[position], position)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.itemLongClickListener(
                holder.itemView,
                dataList[position],
                position
            )
            true
        }
    }

    override fun getItemCount(): Int = dataList.size


    interface OnItemClickListener{
        fun itemClickListener(view: View, recentContact: RecentContact, position: Int)
    }

    interface OnItemLongClickListener{
        fun itemLongClickListener(view: View, recentContact: RecentContact, position: Int)
    }

    var onItemClickListener:OnItemClickListener? = null

    var onItemLongClickListener:OnItemLongClickListener? = null
}