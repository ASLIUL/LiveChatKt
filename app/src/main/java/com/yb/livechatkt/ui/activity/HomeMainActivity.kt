package com.yb.livechatkt.ui.activity

import android.Manifest
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.permissionx.guolindev.PermissionX
import com.yb.livechatkt.LiveChatKtApplication
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.ActivityHomeMainBinding
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast
import com.yb.livechatkt.vm.HomeMainViewModel
import kotlinx.android.synthetic.main.home_bottom_bar.*
import kotlin.math.log

class HomeMainActivity : BaseAppActivity() {

    val viewModel by viewModels<HomeMainViewModel>()
    lateinit var binding:ActivityHomeMainBinding;
    private lateinit var navController: NavController

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.lifecycleOwner = this
        val destinationMap = mapOf(
            R.id.liveFragment to bottom_home_1,
            R.id.sessionFragment to bottom_home_2,
            R.id.contactsFragment to bottom_home_3,
            R.id.mineFragment to bottom_home_4
        )
        navController = findNavController(R.id.fragment)
        destinationMap.forEach { map ->
            map.value.setOnClickListener { navController.navigate(map.key) }
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            controller.popBackStack()
        }
    }

    override fun initListener() {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request{allGranted, grantedList, deniedList ->

            }
        viewModel.tokenFailed.observe(this, Observer {
            tokenFailed(it)
        })
    }

    override fun getLayout(): Int {
        return R.layout.activity_home_main
    }
}