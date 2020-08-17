package com.yb.livechatkt.ui.activity

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.ActivityLoginBinding
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.showToast
import com.yb.livechatkt.vm.LoginViewModel

public class LoginActivity : BaseAppActivity() {

    val viewModel by viewModels<LoginViewModel>()
    lateinit var binding: ActivityLoginBinding;

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.data = viewModel
        binding.lifecycleOwner = this
    }

    override fun initListener() {
        viewModel.loginDataLiveData.observe(this, Observer {
            SaveUserData.get(this).accId =  it.accid
            SaveUserData.get(this).token =  it.token
            SaveUserData.get(this).username =  it.name
            SaveUserData.get(this).id =  it.id
            SaveUserData.get(this).imToken =  it.imToken
            SaveUserData.get(this).qrCodeUrl =  it.qrCodeUrl
            SaveUserData.get(this).role =  it.role
            Log.d(TAG, "initListener: "+it.imToken+"\t"+it.accid)
            viewModel.wyLogin(it.accid,it.imToken)
        })
        viewModel.isLoginSuccess.observe(this, Observer {
            if (it){
                resources.getString(R.string.login_success).showToast()
                startActivity(Intent(this,HomeMainActivity::class.java))
            }else{
                resources.getString(R.string.login_failed).showToast()
            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

}
