package com.yb.livechatkt.ui.model

import android.graphics.Color
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.auth.LoginInfo
import com.yb.livechatkt.LiveChatKtApplication
import com.yb.livechatkt.R
import com.yb.livechatkt.ui.activity.HomeMainActivity
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.SaveUserData

class LiveChatIMHelper {


    companion object{
        fun init() {
            NIMClient.init(LiveChatKtApplication.context,loginInfo(),option())
            // 初始化消息提醒
            NIMClient.toggleNotification(SaveUserData.get(LiveChatKtApplication.context).notification)
        }

        // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
        private fun loginInfo(): LoginInfo? {
            return LoginInfo(
                SaveUserData.get(LiveChatKtApplication.context).accId,
                SaveUserData.get(LiveChatKtApplication.context).token,
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
    }





}