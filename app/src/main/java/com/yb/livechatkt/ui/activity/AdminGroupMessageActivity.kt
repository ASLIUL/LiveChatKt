package com.yb.livechatkt.ui.activity

import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.GroupMessageBean
import com.yb.livechatkt.bean.LiveEnum
import com.yb.livechatkt.bean.ServiceGroup
import com.yb.livechatkt.databinding.ActivityAdminGroupMessageLayoutBinding
import com.yb.livechatkt.ui.adapter.GroupMessageRecyclerAdapter
import com.yb.livechatkt.ui.adapter.SelectGroupRecyclerAdapter
import com.yb.livechatkt.ui.model.GroupInputPanel
import com.yb.livechatkt.ui.model.GroupMSgInputPanelInterface
import com.yb.livechatkt.util.GroupMessageDataComparator
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.SoftKeyBoardListener
import com.yb.livechatkt.vm.AdminGroupMessageViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AdminGroupMessageActivity : BaseAppActivity(),GroupMSgInputPanelInterface {

    val viewModel by viewModels<AdminGroupMessageViewModel>()
    lateinit var binding:ActivityAdminGroupMessageLayoutBinding
    private var serviceGroupMap:MutableMap<String, ServiceGroup> = HashMap()
    private var serviceGroups:MutableList<ServiceGroup> = ArrayList()
    private var accIds:String = ""
    private var groupMessageRecyclerAdapter:GroupMessageRecyclerAdapter? = null
    private var groupMessages:MutableMap<String, GroupMessageBean> = HashMap()
    private var groupMessagesByList:MutableList<GroupMessageBean> = ArrayList()
    private lateinit var groupInputPanel: GroupInputPanel
    private var softKeyBoardListener:SoftKeyBoardListener? = null
    private var keyBoardShow:Boolean = false
    private var selectGroupAdapter:SelectGroupRecyclerAdapter? = null

    override fun initView() {
        binding = DataBindingUtil.setContentView(this, getLayout())
        binding.lifecycleOwner = this
        groupInputPanel = GroupInputPanel(this, binding.root, this)
        val type = object : TypeToken<Map<String, ServiceGroup>>() {}.type
        serviceGroupMap = Gson().fromJson(
            intent.getStringExtra(NetConstant.ANIM_CHOOSER_GROUP_DATA),
            type
        )
        val jsonArray = JsonArray()
        serviceGroupMap.forEach{
            jsonArray.add(it.key)
            serviceGroups.add(it.value)
        }
        accIds = jsonArray.toString()
        var manager = LinearLayoutManager(this)
        manager.orientation = RecyclerView.VERTICAL
        binding.messageList.layoutManager = manager
        groupMessageRecyclerAdapter = GroupMessageRecyclerAdapter(this, groupMessagesByList)
        binding.messageList.adapter = groupMessageRecyclerAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        selectGroupAdapter = SelectGroupRecyclerAdapter(this,serviceGroups)
        binding.chooseGroupList.layoutManager = linearLayoutManager
        binding.chooseGroupList.adapter = selectGroupAdapter

    }

    override fun onStart() {
        super.onStart()
        softKeyBoardListener = SoftKeyBoardListener(this@AdminGroupMessageActivity)
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

    override fun initListener() {
        viewModel.messageStatueLiveData.observe(this, Observer {
            it.keys.forEach { key ->
                if (groupMessages.containsKey(key)) {
                    var groupMessageBean = groupMessages[key]
                    when (it[key]) {
                        LiveEnum.error -> groupMessageBean?.sendState = "发送失败"
                        LiveEnum.success -> groupMessageBean?.sendState = "发送成功"
                        LiveEnum.sending -> groupMessageBean?.sendState = "正在发送"
                    }
                    groupMessages.set(key, groupMessageBean!!)
                }
            }
            updateShowMessage()

        })
        viewModel.fileUpLoadLiveData.observe(this, Observer {
            Log.d(TAG, "initListener: ${it.toString()}")
        })
    }


    private fun updateShowMessage(){
        groupMessagesByList.clear()
        groupMessages.keys.forEach {
            groupMessagesByList.add(groupMessages[it]!!)
        }
        Collections.sort(groupMessagesByList,GroupMessageDataComparator())
        groupMessageRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun    getLayout(): Int = R.layout.activity_admin_group_message_layout

    override fun sendTextMessage(textMessage: GroupMessageBean) {
        groupMessages[textMessage.uuid] = textMessage
        viewModel.sendMessage(textMessage)
        binding.connect.setText("")
        updateShowMessage()
        groupMessageRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun sendImageMessage(imageMessage: GroupMessageBean) {
        groupMessages[imageMessage.uuid] = imageMessage
        updateShowMessage()
        viewModel.uploadFileCreteMessage(imageMessage)
        groupMessageRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun sendVideoMessage(videoMessage: GroupMessageBean) {
        groupMessages[videoMessage.uuid] = videoMessage
        viewModel.uploadFileCreteMessage(videoMessage)
        updateShowMessage()
        groupMessageRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun sendVoiceMessage(voiceMessage: GroupMessageBean) {
        groupMessages[voiceMessage.uuid] = voiceMessage
        viewModel.uploadFileCreteMessage(voiceMessage)
        updateShowMessage()
        groupMessageRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun getToAccIds(): String {
        return accIds
    }

    override fun getKeyBoardIsShow(): Boolean = keyBoardShow

    override fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val v = window.peekDecorView()
        if (null != v) {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun ShowKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.connect, InputMethodManager.SHOW_IMPLICIT)
    }
}