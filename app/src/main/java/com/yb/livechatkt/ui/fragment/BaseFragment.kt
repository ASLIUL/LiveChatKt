package com.yb.livechatkt.ui.fragment

import android.content.Intent
import androidx.fragment.app.Fragment
import com.yb.livechatkt.ui.activity.LoginActivity
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast

open class BaseFragment : Fragment(){


    fun tokenFailed(boolean: Boolean){
        if (boolean) {
            NetConstant.responseTokenFailedMsg.showToast()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}