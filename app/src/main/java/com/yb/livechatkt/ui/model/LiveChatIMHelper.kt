package com.yb.livechatkt.ui.model

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import com.yb.livechatkt.LiveChatKtApplication
import com.yb.livechatkt.R
import com.yb.livechatkt.ui.activity.HomeMainActivity
import com.yb.livechatkt.ui.activity.LoginActivity
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.showToast

class LiveChatIMHelper {

    companion object{
        fun init() {
            NIMClient.init(LiveChatKtApplication.context,loginInfo(),option())
            // 初始化消息提醒
            NIMClient.toggleNotification(SaveUserData.get().notification)
        }

        // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
        private fun loginInfo(): LoginInfo? {
            return LoginInfo(
                SaveUserData.get().accId,
                SaveUserData.get().token,
                NetConstant.IM_APP_KEY
            )
        }

        private fun option():SDKOptions{
            var options = SDKOptions()
            val config = StatusBarNotificationConfig()
            config.notificationEntrance = HomeMainActivity::class.java // 点击通知栏跳转到该Activity
            config.notificationSmallIconId = R.drawable.ic_launcher_foreground
            // 呼吸灯配置
            // 呼吸灯配置
            config.ledARGB = Color.GREEN
            config.ledOnMs = 1000
            config.ledOffMs = 1500
            // 通知铃声的uri字符串
            // 通知铃声的uri字符串
            config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg"
            options.statusBarNotificationConfig = config
            return options

        }
        fun login(context: Context){

            val accid = SaveUserData.get().accId
            val imToken = SaveUserData.get().imToken
            if (accid.isEmpty() || imToken.isEmpty()){
                NetConstant.responseTokenFailedMsg.showToast()
                val intent = Intent(context,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                return
            }
            val info = LoginInfo(
                accid,
                SaveUserData.get().imToken,
                NetConstant.IM_APP_KEY
            )
            val loginInfoRequestCallback: RequestCallback<LoginInfo> =
                object : RequestCallback<LoginInfo> {
                    override fun onSuccess(param: LoginInfo) {
                        Log.d("LiveChatIMHelper", "onSuccess: $param")
                    }

                    override fun onFailed(code: Int) {
                        Log.d("LiveChatIMHelper", "onFailed: $code")

                    }

                    override fun onException(exception: Throwable) {
                        exception.printStackTrace()
                    }
                }
            NIMClient.getService(AuthService::class.java).login(info)
                .setCallback(loginInfoRequestCallback)
        }


    }






}