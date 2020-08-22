package com.yb.livechatkt.view

import android.content.Context
import android.util.AttributeSet
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.yb.livechatkt.R

class NormalGSYVideoPlayer(context: Context,attributeSet: AttributeSet) : StandardGSYVideoPlayer(context,attributeSet) {

    fun startPlay(){
        prepareVideo()
        startDismissControlViewTimer()
    }

    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
        mChangePosition = false
        mChangeVolume = false
        mBrightness = false
    }

    override fun touchDoubleUp() {

    }

    override fun getLayoutId(): Int  = R.layout.video_player_layout
}