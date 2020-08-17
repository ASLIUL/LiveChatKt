package com.yb.livechatkt.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast

public open abstract class BaseAppActivity : AppCompatActivity() {

    val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }
    abstract fun initView()

    abstract fun initListener()


    abstract fun getLayout() : Int;

    fun tokenFailed(boolean: Boolean){
        if (boolean) {
            NetConstant.responseTokenFailedMsg.showToast()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


}