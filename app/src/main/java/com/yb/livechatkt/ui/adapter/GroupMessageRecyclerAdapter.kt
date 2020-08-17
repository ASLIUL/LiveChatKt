package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.speech.tts.Voice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.GroupMessageBean
import com.yb.livechatkt.bean.TextMessage
import com.yb.livechatkt.bean.VoiceMessage
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.util.getBitmapFormUrl

class GroupMessageRecyclerAdapter(var context: Context, private val dataList:List<GroupMessageBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class TextMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.msg_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
    }
    class PicMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:ImageView = itemView.findViewById(R.id.msg_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
    }
    class VoiceMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.audio_length)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
    }
    class VideoMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:ImageView = itemView.findViewById(R.id.video_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
    }
    class TipsMessageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.msg_connect)
        var sendState:TextView = itemView.findViewById(R.id.send_state)
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
                (holder as TextMessageViewHolder).sendState.text = dataList[position].sendState
            }
            1 -> {
                Glide.with(context).load(LiveChatUrl.imgBaseUrl+dataList[position].file).into((holder as PicMessageViewHolder).msgConnect)
            }
            2 -> {
                val voiceMessage: VoiceMessage = gson.fromJson(dataList[position].body,VoiceMessage::class.java)
                (holder as VoiceMessageViewHolder).msgConnect.text = (voiceMessage.dur/1000).toString()
            }
            3 -> {
                Glide.with(context).load(getBitmapFormUrl(LiveChatUrl.imgBaseUrl+dataList[position].file)).into((holder as VideoMessageViewHolder).msgConnect)
            }
            else -> {

            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return dataList[position].type
    }
}