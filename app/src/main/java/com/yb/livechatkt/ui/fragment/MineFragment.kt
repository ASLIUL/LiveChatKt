package com.yb.livechatkt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.FragmentContactsBinding
import com.yb.livechatkt.databinding.FragmentMineBinding
import com.yb.livechatkt.ui.activity.ChooseGroupActivity
import com.yb.livechatkt.ui.activity.LoginActivity
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.showToast
import com.yb.livechatkt.vm.ContactsViewModel
import com.yb.livechatkt.vm.MineViewModel
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    private val TAG = "MineFragment"

    val viewModel by viewModels<MineViewModel>()
    lateinit var binding: FragmentMineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine,container,false)
        binding.data = viewModel
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }
    private fun initView(){
        viewModel.meDataLiveData.observe(viewLifecycleOwner, Observer {
            binding.name.text = it.username
            Glide.with(this).load(it.headImg).into(binding.header)
        })
        viewModel.getMineData()

        viewModel.tokenFailed.observe(viewLifecycleOwner, Observer {
            tokenFailed(it)
        })
        binding.groupMessage.setOnClickListener{
            startActivity(Intent(activity,ChooseGroupActivity::class.java))
        }
        viewModel.isSuccessNoT.observe(requireActivity(), Observer {
            Log.d(TAG, "initView: 退出登录失败了????$it")
            if (it) {
                SaveUserData.get(requireContext()).clearData()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        })
        viewModel.isShowError.observe(viewLifecycleOwner, Observer {
            it.msg.showToast()
        })
    }
}