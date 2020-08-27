package com.yb.livechatkt.ui.activity

import android.Manifest
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import com.permissionx.guolindev.PermissionX
import com.yb.livechatkt.LiveChatKtApplication
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.ActivityHomeMainBinding
import com.yb.livechatkt.ui.model.LiveChatIMHelper
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast
import com.yb.livechatkt.vm.HomeMainViewModel
import kotlinx.android.synthetic.main.contacts_page_bottom.*
import kotlinx.android.synthetic.main.home_bottom_bar.*
import kotlinx.android.synthetic.main.home_page_bottom.*
import kotlinx.android.synthetic.main.mine_page_bottom.*
import kotlinx.android.synthetic.main.session_page_bottom.*
import kotlin.math.log

class HomeMainActivity : BaseAppActivity() {

    val viewModel by viewModels<HomeMainViewModel>()
    lateinit var binding:ActivityHomeMainBinding;
    private lateinit var navController: NavController

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.lifecycleOwner = this
        val destinationMap = mapOf(
            R.id.liveFragment to home_motion_layout,
            R.id.sessionFragment to session_motion_layout,
            R.id.contactsFragment to contacts_motion_layout,
            R.id.mineFragment to mine_motion_layout
        )
        navController = findNavController(R.id.fragment)
        destinationMap.forEach { map ->
            map.value.setOnClickListener { navController.navigate(map.key) }
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            controller.popBackStack()
            destinationMap.values.forEach { it.progress = 0.001f }
            destinationMap[destination.id]?.transitionToEnd()
        }
    }

    override fun initListener() {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request{ _, _, _ -> }

        viewModel.tokenFailed.observe(this, Observer {
            tokenFailed(it)
        })
        viewModel.isShowError.observe(this, Observer {
            it.msg.showToast()
        })
        viewModel.wyLoginMonitor.observe(this, Observer {
            if (!it){
//                NetConstant.responseTokenFailedMsg.showToast()
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
                LiveChatIMHelper.login(this)
            }
        })
        viewModel.isMonitorWyLogin(true)


    }

    override fun getLayout(): Int {
        return R.layout.activity_home_main
    }
}