package com.yb.livechatkt

import android.app.Application
import android.content.Context
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import com.yb.livechatkt.ui.model.LiveChatIMHelper

class LiveChatKtApplication : Application() {

    companion object{
        lateinit var context:Context
        lateinit var instanst:LiveChatKtApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        instanst = this
        EmojiManager.install(GoogleEmojiProvider())
        LiveChatIMHelper.init()
    }


}