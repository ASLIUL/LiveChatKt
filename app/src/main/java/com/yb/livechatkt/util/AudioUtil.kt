package com.yb.livechatkt.util

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AudioUtil(context: Context) {

    //地址
    var audioUrl:String = ""
    set(value) {
        field = value
        stopPlayer()
    }
    private var mediaPlayer:MediaPlayer = MediaPlayer()
    //进度监听
    private val playerProcess = MutableLiveData<Int>()

    //是否正在播放
    private var isPlaying:Boolean = false

    init {
        mediaPlayer.setScreenOnWhilePlaying(true)
    }

    fun startPlayer(){
        try {
            mediaPlayer.setDataSource(audioUrl)
            isPlaying = true
            mediaPlayer.prepareAsync()
            //准备完毕，自动播放
            mediaPlayer.setOnPreparedListener{
                mediaPlayer.start()
            }
            mediaPlayer.setOnErrorListener{ mp,w,e->
                if (isPlaying) mediaPlayer.stop()
                isPlaying = false
                false
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    private fun stopPlayer(){
        if (isPlaying){
            playerProcess.value = 0
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
        }
    }


}