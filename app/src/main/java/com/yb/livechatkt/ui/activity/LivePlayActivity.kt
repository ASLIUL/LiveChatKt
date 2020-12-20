package com.yb.livechatkt.ui.activity

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.*
import com.yb.livechatkt.databinding.ActivityLivePlayLayoutBinding
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.ui.adapter.ChatRoomMessageRecyclerAdapter
import com.yb.livechatkt.ui.adapter.ChatRoomPersonRecyclerAdapter
import com.yb.livechatkt.util.*
import com.yb.livechatkt.view.GiftItemLayout
import com.yb.livechatkt.vm.LivePlayViewModel
import java.util.*
import kotlin.collections.ArrayList

class LivePlayActivity : BaseAppActivity() {

    lateinit var binding:ActivityLivePlayLayoutBinding
    val viewModel by viewModels<LivePlayViewModel>()
    private var liveId:Int = 0
    private var shareUserId:Int = 0
    private var orientationUtils:OrientationUtils? = null
    private var optionBuilder:GSYVideoOptionBuilder? = null
    private val ACTIVITY_RESULT = 1000020
    private var isPlay:Boolean = false
    private var chatRoomMessages:MutableList<ChatRoomTextMessage> = ArrayList()
    private var chatMessageAdapter:ChatRoomMessageRecyclerAdapter? = null
    private var chatRoomPersons:MutableList<ChatRoomPerson> = ArrayList()
    private var chatRoomPersonAdapter:ChatRoomPersonRecyclerAdapter? = null
    private var balance:Double = 0.0
    private val giftData:MutableMap<Int, GiftData> = hashMapOf()
    private var isFirst:Boolean = true
    private var sendGiftNum = 1
    private var sendGiftData:GiftItemData? = null


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
        chatMessageAdapter = ChatRoomMessageRecyclerAdapter(this, chatRoomMessages)
        val manager = LinearLayoutManager(this)
        manager.orientation = RecyclerView.VERTICAL
        binding.messageRecycler.layoutManager = manager
        binding.messageRecycler.adapter = chatMessageAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        binding.watchPerson.layoutManager = linearLayoutManager
        chatRoomPersonAdapter = ChatRoomPersonRecyclerAdapter(this, chatRoomPersons)
        binding.watchPerson.adapter = chatRoomPersonAdapter
        viewModel.getGiftAllType()
        viewModel.getMyWallet()

        val itemLayouts: MutableList<GiftItemLayout> = arrayListOf()
        itemLayouts.add(binding.giftItem1)
        itemLayouts.add(binding.giftItem2)
        itemLayouts.add(binding.giftItem3)
        binding.giftRootLayout.setItemLayout(itemLayouts)

    }

    override fun initListener() {
        viewModel.liveDataLiveData.observe(this, Observer {
            Glide.with(this).load(it.headImg).into(binding.header)
            if (it.isFocus == 1) binding.subscribe.text =
                resources.getString(R.string.subscribe) else if (it.isFocus == 2) binding.subscribe.text =
                resources.getString(R.string.already_subscribe)
            binding.name.text = it.userName
            binding.subNum.text = String.format(
                resources.getString(R.string.already_subscribe),
                it.fansNum.toString()
            )
            initGSYVideoPlayer(it)
            viewModel.joinLiveRoom()
        })
        viewModel.wallet.observe(this){
            balance = it.balance
        }

        binding.msgConnect.setOnClickListener {
            val intent = Intent(this, LivePlayerBottomInputActivity::class.java)
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if (it.resultCode == Activity.RESULT_OK){
                    viewModel.sendTextMessage(
                        it.data?.getStringExtra(NetConstant.CHAT_ROOM_INPUT_CONNECT).toString()
                    )
                }
            }

            //startActivityForResult(intent, ACTIVITY_RESULT)
        }
        binding.close.setOnClickListener {
            viewModel.exitLiveRoom()
            if (isPlay) {
                binding.normalGSYVideoPlayer.currentPlayer.release()
            }
            orientationUtils?.releaseListener()
            finish()
        }


        viewModel.chatRoomMessagesLiveData.observe(this, Observer {
            chatRoomMessages.add(it)
            Collections.sort(chatRoomMessages, ChatRoomMessageDataComparator())
            chatMessageAdapter?.updateShowData()
            binding.messageRecycler.smoothScrollToPosition(chatRoomMessages.size - 1)
        })
        viewModel.roomPersonLiveData.observe(this, Observer {
            chatRoomPersons.clear()
            chatRoomPersons.addAll(it)
            Log.d(TAG, "initListener0: ${it.size}")
            chatRoomPersonAdapter?.notifyDataSetChanged()
        })
        viewModel.isShowError.observe(this, Observer {
            it.msg.showToast()
        })
        viewModel.isOffLineLiveData.observe(this, {
            if (it) {
                offLine();finish()
            }
        })
        viewModel.liveExitRoomLiveData.observe(this, Observer {
            resources.getString(R.string.live_exit).showToast()
        })

        viewModel.chatRoomGiftMsgLiveData.observe(this){
            if (null == it){
                return@observe
            }
            val string = String.format(
                resources.getString(R.string.has_person_send_gift_name),
                it.data.username,
                if (it.data.giftnum == 0) 1 else it.data.giftnum,
                it.data.giftname
            )
            val roomTextMessage = RoomTextMessage(
                string,
                SaveUserData.get().username,
                SaveUserData.get().id.toString(),
                SaveUserData.get().accId,
                SaveUserData.get().role,
                SaveUserData.get().role
            )
            val message = ChatRoomTextMessage(
                NetConstant.responseSuccessCode,
                "",
                System.currentTimeMillis(),
                roomTextMessage
            )
            chatRoomMessages.add(message)
            chatMessageAdapter?.updateShowData()
            binding.messageRecycler.smoothScrollToPosition(chatRoomMessages.size - 1)
            binding.giftRootLayout.loadGift(GiftAnimData(it.data.giftid,it.data.giftnum,it.data.username,it.data.giftname,it.data.giftimg,LiveChatUrl.headerBaseUrl+it.data.chatAccount))
        }

        viewModel.giftTypeLiveData.observe(this){
            giftData.clear()
            if (null == it){
                return@observe
            }
            it.forEach { gift->
                if (!giftData.containsKey(gift.id)){
                    giftData[gift.id] = gift
                }
                Log.d(TAG, "initListener3: ${gift.id}")
                viewModel.getGiftDataByType(gift.id, isFirst)
            }
        }
        viewModel.giftItemDataLiveData.observe(this){
            if (giftData.containsKey(it.records[0].type)){
                giftData[it.records[0].type]?.gifts = it.records
            }
            Log.d(TAG, "initListener6: ${giftData.keys}")
            Log.d(TAG, "initListener7: ${giftData.values}")
        }

        binding.gift.setOnClickListener {
            val titleTabs:MutableList<GiftTitleTab> = arrayListOf()
            giftData.forEach {
                titleTabs.add(GiftTitleTab(it.value.id, it.value.name, 0))
            }
            if(titleTabs.isNotEmpty()){titleTabs[0].check = 1}

            giftData.forEach {
                Log.d(TAG, "initListener4: ${it.value}")
            }

            showGiftDataDialog(this, balance, titleTabs, giftData){ num, gift ->
                if (null == gift) return@showGiftDataDialog
                sendGiftNum = num
                sendGiftData = gift
                viewModel.sendGiftRequest(num, gift)
                viewModel.getMyWallet()
            }
        }
        viewModel.sendGiftLiveData.observe(this){
            if (it){
                viewModel.sendGiftMessage(sendGiftNum, sendGiftData)
            }
        }

    }
    private fun initGSYVideoPlayer(liveRoomBean: LiveRoomBean) {
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this).load(liveRoomBean.cover_img).into(imageView)
        optionBuilder?.setVideoTitle(liveRoomBean.name)?.setUrl(liveRoomBean.rtmpPullUrl)
            ?.setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)
                    orientationUtils?.isEnable = true
                    isPlay = true
                }
            })
            ?.build(binding.normalGSYVideoPlayer)
        binding.normalGSYVideoPlayer.startPlay()
    }

    override fun onBackPressed() {
        viewModel.exitLiveRoom()
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo()
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onResume() {
        binding.normalGSYVideoPlayer.currentPlayer.onVideoResume()
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.exitLiveRoom()
        if (isPlay) {
            binding.normalGSYVideoPlayer.currentPlayer.release()
        }
        orientationUtils?.releaseListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_RESULT){
            if (resultCode == Activity.RESULT_OK){
                viewModel.sendTextMessage(
                    data?.getStringExtra(NetConstant.CHAT_ROOM_INPUT_CONNECT).toString()
                )
            }
        }
    }

    override fun getLayout(): Int = R.layout.activity_live_play_layout
}