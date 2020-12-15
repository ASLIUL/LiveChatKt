package com.yb.livechatkt.ui.adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.RoomGiftMessage
import com.yb.livechatkt.bean.RoomTextMessage


/**
 * create by liu
 * on 2020/6/8 3:08 PM
 */
class GiftShowListViewAdapter(private val context: Context, private val giftBeans: List<RoomGiftMessage>) : BaseAdapter() {

    private val TAG = "GiftShowListViewAdapter"

    override fun getCount(): Int {
        return giftBeans.size
    }

    override fun getItem(position: Int): Any {
        return giftBeans[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        Log.d(TAG, "getView: ")
        val holder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_gift_show_list_view_layout, parent, false)
            holder = ViewHolder()
            holder.imageView = convertView.findViewById(R.id.gift_img)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        Glide.with(context).load(giftBeans[position].giftimg).into(holder.imageView);
        return convertView
    }

    internal inner class ViewHolder {
        lateinit var imageView: ImageView
    }
}