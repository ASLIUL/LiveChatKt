package com.yb.livechatkt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.yb.livechatkt.R
import com.yb.livechatkt.db.DBManager
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    fun offLine(){
        resources.getString(R.string.already_off_line).showToast()
        GlobalScope.launch {
            DBManager.getDBInstance().getFriendsDao().deleteAllData()
            DBManager.getDBInstance().getSessionDao().deleteAllData()
        }
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }


}