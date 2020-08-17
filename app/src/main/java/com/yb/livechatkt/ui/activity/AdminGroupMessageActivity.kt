package com.yb.livechatkt.ui.activity

import android.text.Editable
import android.util.Log
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
import com.yb.livechatkt.ui.model.InputPanel
import com.yb.livechatkt.ui.model.InputPanelInterface
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.vm.AdminGroupMessageViewModel
import kotlin.math.log

class AdminGroupMessageActivity : BaseAppActivity(),InputPanelInterface {

    val viewModel by viewModels<AdminGroupMessageViewModel>()
    lateinit var binding:ActivityAdminGroupMessageLayoutBinding
    var serviceGroups:MutableMap<String,ServiceGroup> = HashMap()
    var accIds:String = ""
    private var groupMessageRecyclerAdapter:GroupMessageRecyclerAdapter? = null
    private var groupMessages:MutableMap<String,GroupMessageBean> = HashMap()
    private var groupMessagesByList:MutableList<GroupMessageBean> = ArrayList()
    lateinit var inputPanel: InputPanel

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.lifecycleOwner = this
        inputPanel = InputPanel(this,binding.root,this)
        val type = object : TypeToken<Map<String,ServiceGroup>>() {}.type
        serviceGroups = Gson().fromJson(intent.getStringExtra(NetConstant.ANIM_CHOOSER_GROUP_DATA),type)
        val jsonArray = JsonArray()
        serviceGroups.forEach{
            jsonArray.add(it.key)
        }
        accIds = jsonArray.toString()
        var manager = LinearLayoutManager(this)
        manager.orientation = RecyclerView.VERTICAL
        binding.messageList.layoutManager = manager
        groupMessageRecyclerAdapter = GroupMessageRecyclerAdapter(this,groupMessagesByList)
        binding.messageList.adapter = groupMessageRecyclerAdapter
    }

    override fun initListener() {
//        binding.send.setOnClickListener {
//            var bean = GroupMessageBean.createTextMessage(binding.connect.text.toString(),accIds)
//            groupMessages[bean.uuid] = bean
//            viewModel.sendMessage(bean)
//            binding.connect.setText("")
//            groupMessageRecyclerAdapter?.notifyDataSetChanged()
//        }
        viewModel.messageStatueLiveData.observe(this, Observer {
            it.keys.forEach{ key ->
                if (groupMessages.containsKey(key)){
                    var groupMessageBean = groupMessages[key]
                    when(it[key]){
                        LiveEnum.error -> groupMessageBean?.sendState = "发送失败"
                        LiveEnum.success -> groupMessageBean?.sendState = "发送成功"
                        LiveEnum.sending -> groupMessageBean?.sendState = "正在发送"
                    }
                    groupMessages.set(key, groupMessageBean!!)
                }
            }
            updateShowMessage()

        })
    }

    private fun updateShowMessage(){
        groupMessagesByList.clear()
        groupMessages.keys.forEach {
            groupMessagesByList.add(groupMessages[it]!!)
        }
        groupMessageRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun    getLayout(): Int = R.layout.activity_admin_group_message_layout

    override fun sendTextMessage(textMessage: GroupMessageBean) {
        groupMessages[textMessage.uuid] = textMessage
        viewModel.sendMessage(textMessage)
        binding.connect.setText("")
        groupMessageRecyclerAdapter?.notifyDataSetChanged()
        viewModel.sendMessage(textMessage)
    }

    override fun sendImageMessage(imageMessage: GroupMessageBean) {

    }

    override fun sendVideoMessage(videoMessage: GroupMessageBean) {

    }

    override fun sendVoiceMessage(voiceMessage: GroupMessageBean) {

    }

    override fun getToAccIds(): String {
        return accIds
    }
}