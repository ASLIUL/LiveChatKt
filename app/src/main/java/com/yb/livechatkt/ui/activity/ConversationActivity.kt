package com.yb.livechatkt.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.UserService
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.HyGroup
import com.yb.livechatkt.bean.RowX
import com.yb.livechatkt.bean.Session
import com.yb.livechatkt.databinding.ActivityConversationLayoutBinding
import com.yb.livechatkt.ui.adapter.ConversationRecyclerAdapter
import com.yb.livechatkt.ui.model.ConversationInputPanel
import com.yb.livechatkt.ui.model.ConversationInputPanelInterface
import com.yb.livechatkt.util.*
import com.yb.livechatkt.vm.ConversationViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ConversationActivity:BaseAppActivity(),ConversationInputPanelInterface {

    private var inputPanel:ConversationInputPanel? = null
    val viewModel by viewModels<ConversationViewModel>()
    private lateinit var binding: ActivityConversationLayoutBinding
    private var messageMap:MutableMap<String, IMMessage> = HashMap()

    private var messageList:MutableList<IMMessage> = ArrayList()
    private var conversationAdapter:ConversationRecyclerAdapter? = null
    private var conversation_type:Int = 1
    private var sessionTypeEnum:SessionTypeEnum = SessionTypeEnum.P2P
    private var acccid:String = ""
    private var keyBoardShow:Boolean = false
    private var softKeyBoardListener: SoftKeyBoardListener? = null
    private var audioUtil:AudioUtil? = null


    override fun initView() {
        binding = DataBindingUtil.setContentView(this, getLayout())
        binding.lifecycleOwner = this
        conversation_type = intent.getIntExtra(NetConstant.SESSION_TYPE, 1)
        Log.d(TAG, "initView: $conversation_type")
        when (conversation_type) {
            NetConstant.SESSION_TEAM -> {
                val hyGroup = intent.getSerializableExtra(NetConstant.CONVERSATION_DATA) as HyGroup
                acccid = hyGroup.tid.toString()
                binding.liveTitleBar.centerTitle.text = hyGroup.tname
                sessionTypeEnum = SessionTypeEnum.Team
                viewModel.getHistoryMessage(acccid, sessionTypeEnum)
            }
            NetConstant.SESSION_P2P -> {
                val rowX = intent.getSerializableExtra(NetConstant.CONVERSATION_DATA) as RowX
                acccid = rowX.accid
                binding.liveTitleBar.centerTitle.text = rowX.name
                sessionTypeEnum = SessionTypeEnum.P2P
                viewModel.getHistoryMessage(acccid, sessionTypeEnum)
            }
            NetConstant.SESSION_SERVICE -> {
                sessionTypeEnum = SessionTypeEnum.P2P
                binding.liveTitleBar.centerTitle.text =
                    resources.getString(R.string.hy_service_person)
                binding.liveTitleBar.rightType = 0
                viewModel.searchHyServicePerson()
            }
            NetConstant.SESSION_HAS_SERVICE_SESSION -> {
                sessionTypeEnum = SessionTypeEnum.P2P
                binding.liveTitleBar.centerTitle.text =
                    resources.getString(R.string.hy_service_person)
                binding.liveTitleBar.rightType = 0
                val session = intent.getSerializableExtra(NetConstant.CONVERSATION_DATA) as RecentContact
                acccid = session.contactId
                viewModel.judgeServiceIsOnLine(acccid)
                viewModel.getHistoryMessage(acccid, sessionTypeEnum)
            }
            NetConstant.SESSION_NORMAL_USER_SESSION -> {
                sessionTypeEnum = SessionTypeEnum.P2P
                val recentContact = intent.getSerializableExtra(NetConstant.CONVERSATION_DATA) as RecentContact
                binding.liveTitleBar.centerTitle.text = NIMClient.getService(UserService::class.java).getUserInfo(recentContact.contactId)?.name
                acccid = recentContact.contactId
                viewModel.getHistoryMessage(acccid, sessionTypeEnum)
            }
        }
        audioUtil = AudioUtil(this)
        viewModel.contactName = binding.liveTitleBar.centerTitle.text.toString()
        inputPanel = ConversationInputPanel(this, binding.root, sessionTypeEnum, this)
        conversationAdapter = ConversationRecyclerAdapter(this, messageList)
        var manager = LinearLayoutManager(this)
        manager.orientation = RecyclerView.VERTICAL
        binding.messageRecycler.layoutManager = manager
        binding.messageRecycler.adapter = conversationAdapter
    }

    override fun initListener() {
        viewModel.receiveMessageLiveData.observe(this, Observer { it ->
            it.forEach {
                messageMap[it.uuid] = it
            }
            viewModel.insertOneSession(it[it.size - 1])
            updateShowMessage()
        })
        viewModel.serviceData.observe(this, Observer {
            //获取客服数据
            if (it.accid.isEmpty()) {
                resources.getString(R.string.current_time_not_service).showToast()
                return@Observer
            }
            acccid = it.accid
            NIMClient.getService(MsgService::class.java).setChattingAccount(acccid, sessionTypeEnum)
            viewModel.getHistoryMessage(acccid, SessionTypeEnum.P2P)
        })
        binding.liveTitleBar.leftImg.setOnClickListener {
            finish()
        }
        viewModel.messageMapLiveData.observe(this, Observer { it ->
            it.forEach {
                if (messageMap.containsKey(it.key)) {
                    messageMap[it.key] = it.value
                }
            }
            updateShowMessage()
        })
        viewModel.isShowError.observe(this, Observer {
            Log.d(TAG, "initListener: ${it.code} \t ${it.msg}")
            it.msg.showToast()
        })
        viewModel.tokenFailed.observe(this, Observer {
            tokenFailed(it)
        })

        viewModel.historyMessageLiveData.observe(this, {
            it.forEach {
                messageMap[it.uuid] = it
            }
            updateShowMessage()
        })
        viewModel.isOffLineLiveData.observe(this,{
            if (it) {offLine();finish()}
        })
        conversationAdapter?.onItemClickListener = object : ConversationRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View, imMessage: IMMessage, position: Int) {
                when(imMessage.msgType){
                    MsgTypeEnum.audio->{
                        val audioMsg = imMessage.attachment as AudioAttachment
                        audioUtil?.audioUrl = audioMsg.url
                        audioUtil?.startPlayer()
                    }
                    MsgTypeEnum.image -> {
                        val imageAttachment = imMessage.attachment as ImageAttachment
                        showImage(imageAttachment.url)
                    }
                }
            }
        }

        conversationAdapter?.onItemLongClickListener = object :ConversationRecyclerAdapter.OnItemLongClickListener{
            override fun onItemLongClick(view: View, imMessage: IMMessage, position: Int) {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        softKeyBoardListener = SoftKeyBoardListener(this@ConversationActivity)
        softKeyBoardListener?.onSoftKeyBoardChangeListener = object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
                keyBoardShow = true
                binding.moreActionLine.visibility = View.GONE
            }

            override fun keyBoardHide(height: Int) {
                keyBoardShow = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //聊天类型记得更改 标志当前页面是聊天页面
        NIMClient.getService(MsgService::class.java).setChattingAccount(acccid, sessionTypeEnum)
    }

    override fun onPause() {
        super.onPause()
        // 退出聊天界面或离开最近联系人列表界面，建议放在onPause中
        NIMClient.getService(MsgService::class.java).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None)
    }

    private fun updateShowMessage(){
        messageList.clear()
        messageMap.forEach{
            messageList.add(it.value)
        }
        Collections.sort(messageList, MessageDataComparator())
        conversationAdapter?.notifyDataSetChanged()
        binding.messageRecycler.scrollToPosition(messageList.size - 1)
    }

    private fun showImage(url:String){
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER
        binding.showImgPager.visibility = View.VISIBLE
        binding.showImgPager.setOnClickListener{
            binding.showImgPager.visibility = View.GONE
        }
        binding.showImgPager.adapter = object : PagerAdapter(){
            override fun getCount(): Int  = 1

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(imageView)
                Glide.with(this@ConversationActivity).load(url).into(imageView)
                return imageView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(imageView)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inputPanel?.onActivityResult(requestCode, resultCode, data)
    }

    override fun getLayout(): Int = R.layout.activity_conversation_layout


    override fun sendTextMessage(textMessage: IMMessage) {
        messageMap[textMessage.uuid] = textMessage
        updateShowMessage()
        viewModel.sendMessage(textMessage)
    }

    override fun sendImageMessage(imageMessage: IMMessage) {
        messageMap[imageMessage.uuid] = imageMessage
        updateShowMessage()
        viewModel.sendMessage(imageMessage)
    }

    override fun sendVideoMessage(videoMessage: IMMessage) {
        messageMap[videoMessage.uuid] = videoMessage
        updateShowMessage()
        viewModel.sendMessage(videoMessage)
    }

    override fun sendVoiceMessage(voiceMessage: IMMessage) {
        messageMap[voiceMessage.uuid] = voiceMessage
        updateShowMessage()
        viewModel.sendMessage(voiceMessage)
    }

    override fun getToAccIds(): String = acccid

    override fun getKeyBoardIsShow(): Boolean = keyBoardShow

    override fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val v = window.peekDecorView()
        if (null != v) {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }


}