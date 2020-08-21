package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.RowX
import com.yb.livechatkt.net.LiveChatUrl

class ContactsListRecyclerAdapter(var context: Context, var dataList: List<RowX>) : RecyclerView.Adapter<ContactsListRecyclerAdapter.ContactsViewHolder>() {

//    interface OnItemClickListener{
//        fun itemClickListener(view: View,rowX: RowX,position: Int)
//    }
//    var onItemClickListener:OnItemClickListener? = null

    private var itemLongClickListener: OnItemLongClickListener? = null
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemLongClickListener {
       fun itemLongClickListener(view: View, rowX: RowX, position: Int)
    }

    interface OnItemClickListener {
        fun itemClickListener(view: View, rowX: RowX, position: Int)
    }

    fun setItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    class ContactsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var header:ImageView = itemView.findViewById(R.id.contacts_header)
        var name:TextView = itemView.findViewById(R.id.contacts_name)
        var letter:TextView = itemView.findViewById(R.id.letter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        var view = LayoutInflater.from(context).inflate(
            R.layout.adapter_contacts_item_layout,
            parent,
            false
        )
        return ContactsViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.letter.visibility = View.GONE
        Glide.with(context).load(LiveChatUrl.headerBaseUrl + dataList[position].accid).into(holder.header)
        holder.name.text = dataList[position].name
        holder.itemView.setOnClickListener {
            itemClickListener?.itemClickListener(it, dataList[position], position)
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener?.itemLongClickListener(it,dataList[position],position)
            true
        }
    }
}