package com.yb.livechatkt.ui.activity

import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.ActivityServiceUpdateHiMsgLayoutBinding
import com.yb.livechatkt.util.showToast
import com.yb.livechatkt.vm.ServiceUpdateMsgViewModel

class ServiceUpdateHiMessageActivity : BaseAppActivity() {

    val viewModel by viewModels<ServiceUpdateMsgViewModel> ()
    lateinit var binding:ActivityServiceUpdateHiMsgLayoutBinding

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.data = viewModel
        binding.lifecycleOwner = this
    }

    override fun initListener() {
        viewModel.hiMsgLiveData.observe(this, Observer {
            viewModel.msgLiveData.value = it[0].message
        })
        viewModel.getMyHiMessage()
        binding.submit.setOnClickListener {
            viewModel.submitData()
        }
        viewModel.updateHiMsgLiveData.observe(this, {
            if (it){
                resources.getString(R.string.update_success).showToast()
            }
        })
        binding.liveTitleBar2.leftImg.setOnClickListener { finish() }
    }

    override fun getLayout(): Int = R.layout.activity_service_update_hi_msg_layout
}