package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.GiftTitleTab

class ShowGiftTitleAdapter(private val context: Context,private val giftData:List<GiftTitleTab>) : RecyclerView.Adapter<ShowGiftTitleAdapter.TitleViewHolder>() {

    class TitleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val title:TextView = itemView.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_show_gift_title_layout,parent,false)
        return TitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        holder.title.text = giftData[position].name
        if (giftData[position].check == 1){
            holder.title.setTextColor(context.resources.getColor(R.color.theme_bg_color))
        }else{
            holder.title.setTextColor(Color.GRAY)
        }

        holder.itemView.setOnClickListener {
            onItemClick(giftData[position])
        }
    }

    override fun getItemCount(): Int  = giftData.size

    private var onItemClick:(GiftTitleTab) ->Unit = {}

    fun setOnItemClickListener(onItemClick:(GiftTitleTab) ->Unit){
        this.onItemClick = onItemClick
    }

}