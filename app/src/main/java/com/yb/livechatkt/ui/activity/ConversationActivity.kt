package com.yb.livechatkt.ui.activity

import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.HyGroup
import com.yb.livechatkt.bean.RowX
import com.yb.livechatkt.databinding.ActivityConversationLayoutBinding
import com.yb.livechatkt.ui.adapter.ConversationRecyclerAdapter
import com.yb.livechatkt.ui.model.ConversationInputPanel
import com.yb.livechatkt.ui.model.ConversationInputPanelInterface
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.vm.ConversationViewModel

class ConversationActivity:BaseAppActivity(),ConversationInputPanelInterface {

    private var groupInputPanel:ConversationInputPanel? = null
    val viewModel by viewModels<ConversationViewModel>()
    private lateinit var binding: ActivityConversationLayoutBinding
    private var messageList:MutableList<IMMessage> = ArrayList()
    private var conversationAdapter:ConversationRecyclerAdapter? = null
    private var conversation_type:Int = 1
    private var sessionTypeEnum:SessionTypeEnum = SessionTypeEnum.P2P
    private var acccid:String = ""

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.lifecycleOwner = this
        conversation_type = intent.getIntExtra(NetConstant.SESSION_TYPE,1)
        if (conversation_type == 2){
            val hyGroup = intent.getSerializableExtra(NetConstant.CONVERSATION_DATA) as HyGroup
            acccid = hyGroup.tid.toString()
            binding.liveTitleBar.centerTitle.text = hyGroup.tname
            sessionTypeEnum = SessionTypeEnum.Team
        }else if (conversation_type == 1){
            val rowX = intent.getSerializableExtra(NetConstant.CONVERSATION_DATA) as RowX
            acccid = rowX.accid
            binding.liveTitleBar.centerTitle.text = rowX.name
            sessionTypeEnum = SessionTypeEnum.P2P
        }else if (conversation_type == 3){
            sessionTypeEnum = SessionTypeEnum.P2P
            binding.liveTitleBar.centerTitle.text = resources.getString(R.string.hy_service_person)
            binding.liveTitleBar.rightType = 0
            viewModel.searchHyServicePerson()
        }

        groupInputPanel = ConversationInputPanel(this,binding.root,sessionTypeEnum,this)
        conversationAdapter = ConversationRecyclerAdapter(this,messageList)
        var manager = LinearLayoutManager(this)
        manager.orientation = RecyclerView.VERTICAL
        binding.messageRecycler.layoutManager = manager
        binding.messageRecycler.adapter = conversationAdapter
    }

    override fun initListener() {
        viewModel.receiveMessageLiveData.observe(this, Observer {
            messageList.addAll(it)
            conversationAdapter?.notifyDataSetChanged()
        })
        viewModel.serviceData.observe(this, Observer {
            //获取客服数据
            if (it.accid.isEmpty()) {
                Log.d(TAG, "initListener: 获取客服遇到错误")
            }
            acccid = it.accid
        })
        binding.liveTitleBar.leftImg.setOnClickListener {
            finish()
        }
        viewModel.messageMapLiveData.observe(this, Observer {
            conversationAdapter?.notifyDataSetChanged()
        })
    }

    override fun getLayout(): Int = R.layout.activity_conversation_layout


    override fun sendTextMessage(textMessage: IMMessage) {
        messageList.add(textMessage)
        conversationAdapter?.notifyDataSetChanged()
        viewModel.sendMessage(textMessage)
    }

    override fun sendImageMessage(imageMessage: IMMessage) {
        messageList.add(imageMessage)
        conversationAdapter?.notifyDataSetChanged()
        viewModel.sendMessage(imageMessage)
    }

    override fun sendVideoMessage(videoMessage: IMMessage) {
        messageList.add(videoMessage)
        conversationAdapter?.notifyDataSetChanged()
        viewModel.sendMessage(videoMessage)
    }

    override fun sendVoiceMessage(voiceMessage: IMMessage) {
        messageList.add(voiceMessage)
        conversationAdapter?.notifyDataSetChanged()
        viewModel.sendMessage(voiceMessage)
    }

    override fun getToAccIds(): String = acccid


}