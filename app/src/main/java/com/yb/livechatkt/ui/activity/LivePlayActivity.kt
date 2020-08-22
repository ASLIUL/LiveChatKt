package com.yb.livechatkt.ui.activity

import android.content.Intent
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.LiveRoomBean
import com.yb.livechatkt.databinding.ActivityLivePlayLayoutBinding
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.vm.LivePlayViewModel

class LivePlayActivity : BaseAppActivity() {

    lateinit var binding:ActivityLivePlayLayoutBinding
    val viewModel by viewModels<LivePlayViewModel>()
    private var liveId:Int = 0
    private var shareUserId:Int = 0
    private var orientationUtils:OrientationUtils? = null
    private var optionBuilder:GSYVideoOptionBuilder? = null

    override fun initView() {
        binding = DataBindingUtil.setContentView(this, getLayout())
        binding.lifecycleOwner = this
        liveId = intent.getIntExtra(NetConstant.LIVE_ID, 0)
        shareUserId = intent.getIntExtra(NetConstant.SHARE_LIVE_USER_ID, 0)
        if (shareUserId>0){
            viewModel.searchLiveByLiveId(true, liveId, shareUserId)
        }else{
            viewModel.searchLiveByLiveId(true, liveId, shareUserId)
        }
        orientationUtils = OrientationUtils(this, binding.normalGSYVideoPlayer)
        orientationUtils?.isEnable = true
        optionBuilder = GSYVideoOptionBuilder()
        optionBuilder?.setIsTouchWiget(true)
            ?.setRotateViewAuto(false)
            ?.setLockLand(false)
            ?.setAutoFullWithSize(true)
            ?.setShowFullAnimation(true)
            ?.setLooping(false)
    }

    override fun initListener() {
        viewModel.liveDataLiveData.observe(this, Observer {
            initGSYVideoPlayer(it)
            viewModel.joinLiveRoom()
        })
        binding.msgConnect.setOnClickListener {
            val intent = Intent(this, LivePlayerBottomInputActivity::class.java)
            startActivity(intent)
        }
    }
    private fun initGSYVideoPlayer(liveRoomBean: LiveRoomBean) {
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this).load(liveRoomBean.cover_img).into(imageView)
        optionBuilder?.setVideoTitle(liveRoomBean.name)?.setUrl(liveRoomBean.rtmpPullUrl)
            ?.build(binding.normalGSYVideoPlayer)
        binding.normalGSYVideoPlayer.startPlay()
    }

    override fun onBackPressed() {

        viewModel.exitLiveRoom()
        super.onBackPressed()
    }

    override fun getLayout(): Int = R.layout.activity_live_play_layout
}