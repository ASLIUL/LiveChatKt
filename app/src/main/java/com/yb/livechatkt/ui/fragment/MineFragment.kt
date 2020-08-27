package com.yb.livechatkt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.FragmentMineBinding
import com.yb.livechatkt.db.DBManager
import com.yb.livechatkt.ui.activity.*
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.QRCodeUtil
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.showToast
import com.yb.livechatkt.vm.MineViewModel

class MineFragment : BaseFragment() {

    private val TAG = "MineFragment"

    val viewModel by viewModels<MineViewModel>()
    lateinit var binding: FragmentMineBinding
    //在线状态 1去上线，2 去下线
    private var status:String = "2"


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
        viewModel.wyLoginMonitor.observe(viewLifecycleOwner, Observer {
            wyLoginFailed(it)
        })
        binding.groupMessage.setOnClickListener{
            startActivity(Intent(activity,ChooseGroupActivity::class.java))
        }
        binding.qrCode.setOnClickListener {
            val intent = Intent(activity, QRCodeActivity::class.java)
            startActivity(intent)
        }
        binding.updateHiMsg.setOnClickListener {
            var intent = Intent(requireContext(),ServiceUpdateHiMessageActivity::class.java)
            startActivity(intent)
        }
        viewModel.isShowError.observe(viewLifecycleOwner, Observer {
            it.msg.showToast()
        })
        binding.findService.setOnClickListener {
            var intent = Intent(requireContext(),ConversationActivity::class.java)
            intent.putExtra(NetConstant.SESSION_TYPE,NetConstant.SESSION_SERVICE)
            startActivity(intent)
        }
        binding.service.setOnClickListener {
            viewModel.serviceState(status)
        }
        viewModel.exitLiveData.observe(requireActivity(), Observer {
            if (it) {
                SaveUserData.get().clearData()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        })
        binding.relativeLayout.setOnClickListener {
            val intent = Intent(activity, UpdateUserDataActivity::class.java)
            intent.putExtra("meData",viewModel.meDataLiveData.value)
            startActivity(intent)
        }
        viewModel.userNotDataLiveData.observe(requireActivity(), Observer {
            if (it){
                resources.getString(R.string.please_complete_data).showToast()
                startActivity(Intent(requireContext(),CompleteInformationActivity::class.java))
            }
        })

        viewModel.updateStatusLiveData.observe(requireActivity(), Observer {
            if (it){
                status = if (status == "1")  "2" else "1"
                if (status == "1") {
                    binding.service.title.text = String.format(
                        resources.getString(R.string.service_status),
                        resources.getString(R.string.up)
                    )
                    String.format(
                        resources.getString(R.string.update_line_success),
                        resources.getString(R.string.down)
                    ).showToast()
                }else {
                    binding.service.title.text = String.format(
                        resources.getString(R.string.service_status),
                        resources.getString(R.string.down)
                    )
                    String.format(
                        resources.getString(R.string.update_line_success),
                        resources.getString(R.string.up)
                    ).showToast()
                }
            }else{
                if (status == "1")
                    String.format(resources.getString(R.string.update_line_failed),resources.getString(R.string.up)).showToast()
                else
                    String.format(resources.getString(R.string.update_line_failed),resources.getString(R.string.down)).showToast()
            }
        })

    }
}