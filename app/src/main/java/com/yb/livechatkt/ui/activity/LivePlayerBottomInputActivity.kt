package com.yb.livechatkt.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.ActivityLivePlayerInputPanelLayoutBinding
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast
import java.util.*

class LivePlayerBottomInputActivity : BaseAppActivity() {

    lateinit var binding:ActivityLivePlayerInputPanelLayoutBinding

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.BOTTOM)
    }

    override fun initListener() {


        Handler().postDelayed({
            binding.msgConnect.isEnabled = true
            binding.msgConnect.isFocusableInTouchMode = true
            binding.msgConnect.requestFocus()
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(binding.msgConnect,0)

        },200)

        binding.sendMsg.setOnClickListener {
            if (binding.msgConnect.text?.isEmpty()!!){
                resources.getString(R.string.write_something).showToast()
            }else{
                hideSoftInput()
                val intent = Intent()
                Log.d(TAG, "initListener: ${binding.msgConnect.text.toString()}")
                intent.putExtra(NetConstant.CHAT_ROOM_INPUT_CONNECT,binding.msgConnect.text.toString())
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
    }

    private fun hideSoftInput(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = window.peekDecorView()
        inputManager.hideSoftInputFromWindow(view?.windowToken,0)
    }

    override fun getLayout(): Int = R.layout.activity_live_player_input_panel_layout
}