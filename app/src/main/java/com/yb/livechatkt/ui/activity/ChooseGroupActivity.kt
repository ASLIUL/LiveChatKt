package com.yb.livechatkt.ui.activity

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.ServiceGroup
import com.yb.livechatkt.databinding.ActivityChooseGroupLayoutBinding
import com.yb.livechatkt.ui.adapter.GroupDataLiveRecyclerAdapter
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast
import com.yb.livechatkt.vm.ChooseGroupViewModel

class ChooseGroupActivity : BaseAppActivity() {

    val viewModel by viewModels<ChooseGroupViewModel>()
    lateinit var binding:ActivityChooseGroupLayoutBinding
    private var groupList:MutableList<ServiceGroup> = ArrayList()
    private var adapter:GroupDataLiveRecyclerAdapter? = null

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.lifecycleOwner = this
        var manager = LinearLayoutManager(this)
        manager.orientation = RecyclerView.VERTICAL
        binding.groupList.layoutManager = manager
        adapter = GroupDataLiveRecyclerAdapter(this,groupList)
        binding.groupList.adapter = adapter
    }

    override fun initListener() {
        viewModel.groupDataLiveData.observe(this, Observer {
            groupList.clear()
            groupList.addAll(it)
            adapter?.notifyDataSetChanged()
        })
        viewModel.getAllGroup()
        viewModel.isShowError.observe(this, Observer {
            it.msg.showToast()
        })
        binding.liveTitleBar.rightTitle.setOnClickListener {
            val groupData = adapter?.getChooseGroup()
            if (groupData?.isEmpty()!!){
                NetConstant.PLEASE_CHOOSE_GROUP_DATA.showToast()
                return@setOnClickListener
            }
            val json = Gson().toJson(adapter?.getChooseGroup())
            var intent = Intent(this,AdminGroupMessageActivity::class.java)
            intent.putExtra(NetConstant.ANIM_CHOOSER_GROUP_DATA,json)
            startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_choose_group_layout
    }
}