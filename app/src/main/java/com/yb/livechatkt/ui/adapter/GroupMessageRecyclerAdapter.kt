package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.speech.tts.Voice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.GroupMessageBean
import com.yb.livechatkt.bean.TextMessage
import com.yb.livechatkt.bean.VoiceMessage
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.getBitmapFormUrl
import java.io.File

class  GroupMessageRecyclerAdapter(var context: Context, private val dataList:List<GroupMessageBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class TextMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.msg_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class PicMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:ImageView = itemView.findViewById(R.id.msg_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
        var msgLine:LinearLayout = itemView.findViewById(R.id.linearLayout3)
    }
    class VoiceMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.audio_length)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class VideoMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:ImageView = itemView.findViewById(R.id.video_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class TipsMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.msg_connect)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view:View
        when(viewType){
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_send_text_layout,parent,false)
                return TextMessageViewHolder(view)
            }
            1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_send_pic_layout,parent,false)
                return PicMessageViewHolder(view)
            }
            2 -> {
                view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_send_voice_layout,parent,false)
                return VoiceMessageViewHolder(view)
            }
            3 -> {
                view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_send_video_layout,parent,false)
                return VideoMessageViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_tips_layout,parent,false)
                return TipsMessageViewHolder(view)
            }
        }



    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val gson = Gson()
        when(getItemViewType(position)){
            0 -> {
                var textMessage:TextMessage = gson.fromJson(dataList[position].body,TextMessage::class.java)
                (holder as TextMessageViewHolder).msgConnect.text = textMessage.msg
                holder.sendState.text = dataList[position].sendState
                Glide.with(context).load(R.mipmap.group_header).into(holder.header)
                holder.name.text = SaveUserData.get().username+""
            }
            1 -> {
                Glide.with(context).load(dataList[position].filepPath).into((holder as PicMessageViewHolder).msgConnect)
                Glide.with(context).load(R.mipmap.group_header).into(holder.header)
                holder.name.text = SaveUserData.get().username+""
                holder.sendState.text = dataList[position].sendState
            }
            2 -> {
                val voiceMessage: VoiceMessage = gson.fromJson(dataList[position].body,VoiceMessage::class.java)
                (holder as VoiceMessageViewHolder).msgConnect.text = (voiceMessage.dur/1000).toString()
                Glide.with(context).load(R.mipmap.group_header).into(holder.header)
                holder.name.text = SaveUserData.get().username+""
                holder.sendState.text = dataList[position].sendState
            }
            3 -> {
                Glide.with(context).load(Uri.fromFile(File(dataList[position].filepPath))).into((holder as VideoMessageViewHolder).msgConnect)
                Glide.with(context).load(R.mipmap.group_header).into(holder.header)
                holder.name.text = SaveUserData.get().username+""
                holder.sendState.text = dataList[position].sendState
            }
            else -> {

            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return dataList[position].type
    }
}