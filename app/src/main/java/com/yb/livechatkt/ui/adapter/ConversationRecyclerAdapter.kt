package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.yb.livechatkt.R
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.ui.model.LiveCustomAttachment
import com.yb.livechatkt.ui.model.ShareLiveMessageAttachment
import java.lang.Exception

class ConversationRecyclerAdapter(val context: Context,val dataList:List<IMMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SEND_TEXT_VIEW = 0
    private val RECEIVE_TEXT_VIEW = 1
    private val SEND_PIC_VIEW = 2
    private val RECEIVE_PIC_VIEW = 3
    private val SEND_VIDEO_VIEW = 4
    private val RECEIVE_VIDEO_VIEW = 5
    private val SEND_VOICE_VIEW = 7
    private val RECEIVE_VOICE_VIEW = 8
    private val SEND_LIVE_SHARE_VIEW = 9
    private val RECEIVE_LIVE_SHARE_VIEW = 10
    private val TIP_MESSAGE = 11



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            SEND_TEXT_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_send_text_layout,parent,false)
                SendTextMessageViewHolder(view)
            }
            SEND_PIC_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_send_pic_layout,parent,false)
                SendPicMessageViewHolder(view)
            }
            SEND_VIDEO_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_send_video_layout,parent,false)
                SendVideoMessageViewHolder(view)
            }
            SEND_VOICE_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_send_voice_layout,parent,false)
                SendVoiceMessageViewHolder(view)
            }
            SEND_LIVE_SHARE_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_send_live_share_layout,parent,false)
                SendShareLiveMessageViewHolder(view)
            }

            RECEIVE_TEXT_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_received_text_layout,parent,false)
                ReceiveTextMessageViewHolder(view)
            }
            RECEIVE_PIC_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_received_pic_layout,parent,false)
                ReceivePicMessageViewHolder(view)
            }
            RECEIVE_VIDEO_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_received_video_layout,parent,false)
                ReceiveVideoMessageViewHolder(view)
            }
            RECEIVE_VOICE_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_received_voice_layout,parent,false)
                ReceiveVoiceMessageViewHolder(view)
            }
            RECEIVE_LIVE_SHARE_VIEW ->{
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_received_live_share_layout,parent,false)
                ReceiveShareLiveMessageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_conversation_tips_layout,parent,false)
                TipMessageViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            when(getItemViewType(position)){
                SEND_TEXT_VIEW -> {
                    Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into((holder as SendTextMessageViewHolder).header)
                    holder.name.text = dataList[position].fromNick
                    holder.msgConnect.text = dataList[position].content
                    holder.state.visibility = View.GONE
                    when(dataList[position].status) {
                        MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                        MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                        MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                    }
                }
                RECEIVE_TEXT_VIEW -> {
                    Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into((holder as ReceiveTextMessageViewHolder).header)
                    holder.name.text = dataList[position].fromNick
                    holder.msgConnect.text = dataList[position].content
                    holder.state.visibility = View.GONE
                    when(dataList[position].status) {
                        MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                        MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                        MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                    }
                }
                SEND_PIC_VIEW ->{
                    val imageAttachment = dataList[position].attachment as ImageAttachment
                    Glide.with(context).load(imageAttachment.thumbUrl).into((holder as SendPicMessageViewHolder).msgConnect)
                    Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into(holder.header)
                    holder.name.text = dataList[position].fromNick
                    holder.state.visibility = View.GONE
                    when(dataList[position].status) {
                        MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                        MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                        MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                    }
                }
                RECEIVE_PIC_VIEW -> {
                    val imageAttachment = dataList[position].attachment as ImageAttachment
                    Glide.with(context).load(imageAttachment.thumbUrl).into((holder as ReceivePicMessageViewHolder).msgConnect)
                    Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into(holder.header)
                    holder.name.text = dataList[position].fromNick
                    holder.state.visibility = View.GONE
                    when(dataList[position].status) {
                        MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                        MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                        MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                    }
                }
                SEND_VOICE_VIEW -> {
                    Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into((holder as SendVoiceMessageViewHolder).header)
                    val audioAttachment = dataList[position].attachment as AudioAttachment
                    holder.msgConnect.text = (audioAttachment.duration/1000).toString()
                    holder.name.text = dataList[position].fromNick
                    holder.state.visibility = View.GONE
                    when(dataList[position].status) {
                        MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                        MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                        MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                    }
                }
                RECEIVE_VOICE_VIEW -> {
                    Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into((holder as ReceiveVoiceMessageViewHolder).header)
                    val audioAttachment = dataList[position].attachment as AudioAttachment
                    holder.msgConnect.text = (audioAttachment.duration/1000).toString()
                    holder.name.text = dataList[position].fromNick
                    holder.state.visibility = View.GONE
                    when(dataList[position].status) {
                        MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                        MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                        MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                    }
                }
                SEND_VIDEO_VIEW -> {
                    val videoAttachment = dataList[position].attachment as VideoAttachment
                    Glide.with(context).load(videoAttachment.thumbUrl).into((holder as SendVideoMessageViewHolder).msgConnect)
                    Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into(holder.header)
                    holder.name.text = dataList[position].fromNick
                    holder.state.visibility = View.GONE
                    when(dataList[position].status) {
                        MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                        MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                        MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                    }
                }
                RECEIVE_VIDEO_VIEW ->{
                    val videoAttachment = dataList[position].attachment as VideoAttachment
                    Glide.with(context).load(videoAttachment.thumbUrl).into((holder as ReceiveVideoMessageViewHolder).msgConnect)
                    Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into(holder.header)
                    holder.name.text = dataList[position].fromNick
                    holder.state.visibility = View.GONE
                    when(dataList[position].status) {
                        MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                        MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                        MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                    }
                }
                SEND_LIVE_SHARE_VIEW -> {
                    val customMessage = dataList[position].attachment as LiveCustomAttachment
                    if (customMessage is ShareLiveMessageAttachment){
                        Glide.with(context).load(customMessage.liveShareMessageBean?.liveCover).into((holder as SendShareLiveMessageViewHolder).liveCover)
                        Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into(holder.header)
                        holder.liveTitle.text = customMessage.liveShareMessageBean?.liveTitle
                        holder.msgTitle.text = customMessage.liveShareMessageBean?.liveName + context.resources.getString(R.string.liveing)
                        holder.name.text = dataList[position].fromNick
                        holder.state.visibility = View.GONE
                        when(dataList[position].status) {
                            MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                            MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                            MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                        }
                    }
                }
                RECEIVE_LIVE_SHARE_VIEW -> {
                    val customMessage = dataList[position].attachment as LiveCustomAttachment
                    if (customMessage is ShareLiveMessageAttachment){
                        Glide.with(context).load(customMessage.liveShareMessageBean?.liveCover).into((holder as ReceiveShareLiveMessageViewHolder).liveCover)
                        Glide.with(context).load(LiveChatUrl.headerBaseUrl+dataList[position].fromAccount).into(holder.header)
                        holder.liveTitle.text = customMessage.liveShareMessageBean?.liveTitle
                        holder.msgTitle.text = customMessage.liveShareMessageBean?.liveName + context.resources.getString(R.string.liveing)
                        holder.name.text = dataList[position].fromNick
                        holder.state.visibility = View.GONE
                        when(dataList[position].status) {
                            MsgStatusEnum.sending -> holder.state.text = context.resources.getString(R.string.sending)
                            MsgStatusEnum.fail -> holder.state.text = context.resources.getString(R.string.send_failed)
                            MsgStatusEnum.success -> holder.state.text = context.resources.getString(R.string.send_success)
                        }
                    }
                }
                TIP_MESSAGE -> {
                    (holder as TipMessageViewHolder).msgConnect.text = dataList[position].content
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        if (dataList[position].direct == MsgDirectionEnum.In){

            return when(dataList[position].msgType){
                MsgTypeEnum.text -> RECEIVE_TEXT_VIEW
                MsgTypeEnum.audio -> RECEIVE_VOICE_VIEW
                MsgTypeEnum.image -> RECEIVE_PIC_VIEW
                MsgTypeEnum.custom -> RECEIVE_LIVE_SHARE_VIEW
                MsgTypeEnum.video -> RECEIVE_VIDEO_VIEW
                else -> TIP_MESSAGE
            }
        }else{
            return when(dataList[position].msgType){
                MsgTypeEnum.text -> SEND_TEXT_VIEW
                MsgTypeEnum.audio -> SEND_VOICE_VIEW
                MsgTypeEnum.image -> SEND_PIC_VIEW
                MsgTypeEnum.custom -> SEND_LIVE_SHARE_VIEW
                MsgTypeEnum.video -> SEND_VIDEO_VIEW
                else -> TIP_MESSAGE
            }
        }

    }

    override fun getItemId(position: Int): Long {
        return dataList[position].serverId
    }

    class SendTextMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.msg_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class ReceiveTextMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.msg_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class SendVideoMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var msgConnect:ImageView = itemView.findViewById(R.id.video_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class ReceiveVideoMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var msgConnect:ImageView = itemView.findViewById(R.id.video_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class SendVoiceMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.audio_length)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class ReceiveVoiceMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.audio_length)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class SendPicMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var msgConnect:ImageView = itemView.findViewById(R.id.msg_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class ReceivePicMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var msgConnect:ImageView = itemView.findViewById(R.id.msg_connect)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class SendShareLiveMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var liveCover:ImageView = itemView.findViewById(R.id.live_cover)
        var msgTitle:TextView = itemView.findViewById(R.id.msg_title)
        var liveTitle:TextView = itemView.findViewById(R.id.live_title)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class ReceiveShareLiveMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var liveCover:ImageView = itemView.findViewById(R.id.live_cover)
        var msgTitle:TextView = itemView.findViewById(R.id.msg_title)
        var liveTitle:TextView = itemView.findViewById(R.id.live_title)
        var header:ImageView = itemView.findViewById(R.id.header_img)
        var state:TextView = itemView.findViewById(R.id.send_state)
        var name:TextView = itemView.findViewById(R.id.name)
    }
    class TipMessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var msgConnect:TextView = itemView.findViewById(R.id.msg_connect)
    }


}