package com.yb.livechatkt.ui.model

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.netease.nimlib.sdk.media.record.AudioRecorder
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback
import com.netease.nimlib.sdk.media.record.RecordType
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.permissionx.guolindev.PermissionX
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.ActionType
import com.yb.livechatkt.bean.PlugBean
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.ui.adapter.PlugRecyclerAdapter
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.showToast
import java.io.File
import java.io.IOException

class ConversationInputPanel(
    private var activity: AppCompatActivity,
    rootView: View,
    val sessionTypeEnum: SessionTypeEnum,
    private var inputPanelInterface: ConversationInputPanelInterface
) : IAudioRecordCallback {

    private var timer: Chronometer = rootView.findViewById(R.id.recording_time)
    private var recordTips: TextView = rootView.findViewById(R.id.record_tips)
    private var recordLine: LinearLayout = rootView.findViewById(R.id.recording_line)
    private var moreAction: ImageView = rootView.findViewById(R.id.more_action)
    private var moreActionLine: LinearLayout = rootView.findViewById(R.id.more_action_line)
    private var actionRecycler: RecyclerView = rootView.findViewById(R.id.action_bar)
    private var msgConnect:EditText = rootView.findViewById(R.id.connect)
    private var send:TextView = rootView.findViewById(R.id.send)
    private val touchRecord:TextView = rootView.findViewById(R.id.touch_recording)
    private val voice:ImageView = rootView.findViewById(R.id.voice)
    private val TAG = "ConversationInputPanel"
    private val messageRemoteMap:MutableMap<String,Any> = HashMap()

    var plugRecyclerAdapter : PlugRecyclerAdapter? = null

    //1 图片 2语音，3视频
    private var messageType:Int = 1


    //是否开始录音
    private var started:Boolean = false
    //是否正在录音
    //private var isRecordAudio:Boolean = false
    //是否取消录音
    private var isCancelRecordAudio:Boolean = true

    //是否按着录音TV
    private var touched:Boolean = false

    var audioRecorder: AudioRecorder? = null
    //录音最长时间
    val maxTime:Int = 120

    private val picCode:Int = 100001
    private val videoCode:Int = 100002
    private val cameraCode:Int = 100004

    init {

        messageRemoteMap["name"] = SaveUserData.get().username+""
        messageRemoteMap["pic"] = LiveChatUrl.headerBaseUrl+SaveUserData.get().accId
        val plugAlbum = PlugBean(ActionType.ALUBM)
        //val plugShot = PlugBean(ActionType.SHOT)
        val plugVideo = PlugBean(ActionType.VIDEO)
        val plugList:MutableList<PlugBean> = ArrayList()
        plugList.add(plugAlbum)
        //plugList.add(plugShot)
        plugList.add(plugVideo)

        plugRecyclerAdapter = PlugRecyclerAdapter(context = activity,plugList)
        var gridLayoutManager = StaggeredGridLayoutManager(4,LinearLayout.VERTICAL)
        actionRecycler.layoutManager = gridLayoutManager
        actionRecycler.adapter = plugRecyclerAdapter
        send.setOnClickListener {
            sendTextMessage()
        }
        touchRecord.setOnTouchListener { view, motionEvent -> {

        }

            PermissionX.init(activity).permissions(Manifest.permission.RECORD_AUDIO)
                .request{ allGranted, grantedList, deniedList ->
                    if (allGranted){
                        when(motionEvent.action){
                            MotionEvent.ACTION_DOWN -> {
                                touched = true
                                //isRecordAudio = true
                                audioRecorder = AudioRecorder(activity, RecordType.AAC, maxTime, this)
                                activity.window.setFlags(
                                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                                )
                                audioRecorder?.startRecord()
                                isCancelRecordAudio = false
                            }
                            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                                touched = false
                                //isRecordAudio = false
                                endRecord(judeIsCancelRecord(view, motionEvent))
                            }
                            MotionEvent.ACTION_MOVE -> {
                                touched = true
                                //isRecordAudio = true
                                cancelRecord(judeIsCancelRecord(view, motionEvent))
                            }

                        }
                    }
                }





            false
        }
        voice.setOnClickListener {
            if (touchRecord.visibility == View.VISIBLE){
                touchRecord.visibility = View.GONE
                msgConnect.visibility = View.VISIBLE
                send.visibility = View.VISIBLE
                Glide.with(activity).load(R.drawable.ic_voice_black).into(voice)
            }else{
                touchRecord.visibility = View.VISIBLE
                msgConnect.visibility = View.GONE
                send.visibility = View.GONE
                Glide.with(activity).load(R.drawable.ic_key_board_black).into(voice)
            }
        }
        moreAction.setOnClickListener {
            if (inputPanelInterface.getKeyBoardIsShow()){
                inputPanelInterface.hideKeyBoard()
            }
            if (moreActionLine.visibility == View.VISIBLE) moreActionLine.visibility = View.GONE else moreActionLine.visibility = View.VISIBLE
        }
        plugRecyclerAdapter?.onItemClickListener = object : PlugRecyclerAdapter.OnItemClickListener{
            override fun itemClick(view: View, plugBean: PlugBean, position: Int) {
                when(plugBean.action){
                    ActionType.ALUBM ->{

                    }
                }
            }
        }
    }

    private fun sendTextMessage(){
        if (msgConnect.text.toString().isEmpty()){
            activity.resources.getString(R.string.not_send_null_text).showToast()
            return
        }
        if (inputPanelInterface.getToAccIds().isEmpty()){
            activity.resources.getString(R.string.loading_service_person).showToast()
            return
        }
        var textMessage = MessageBuilder.createTextMessage(
            inputPanelInterface.getToAccIds(),
            sessionTypeEnum,
            msgConnect.text.toString()
        )
        textMessage.remoteExtension = messageRemoteMap
        msgConnect.setText("")
        inputPanelInterface.sendTextMessage(textMessage)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (resultCode != Activity.RESULT_OK){
            return
        }
        var mediaDatas:MutableList<LocalMedia> = ArrayList()
        var resultDatas:MutableList<String> = ArrayList()
        mediaDatas.clear()
        mediaDatas.addAll(PictureSelector.obtainMultipleResult(data))
        when(requestCode){
            picCode -> messageType = 1
            videoCode -> messageType = 3
            cameraCode -> messageType = 1
        }
        mediaDatas.forEach {
            resultDatas.add(it.path)
        }
        createFileMessage(resultDatas)
    }

    private fun createFileMessage(fileDatas: List<String>){
        when(messageType){
            1 -> {
                var message = MessageBuilder.createImageMessage(
                    inputPanelInterface.getToAccIds(), sessionTypeEnum, File(
                        fileDatas[0]
                    )
                )
                message.remoteExtension = messageRemoteMap
                inputPanelInterface.sendImageMessage(message)
            }
            3 -> {
                var file = File(fileDatas[0])
                var videoData = getVideoDuration(fileDatas[0])
                var message = MessageBuilder.createVideoMessage(
                    inputPanelInterface.getToAccIds(), sessionTypeEnum,
                    file,
                    videoData[0],
                    videoData.get(1).toInt(),
                    videoData.get(2).toInt(),
                    file.name
                )
                message.remoteExtension = messageRemoteMap
                inputPanelInterface.sendVideoMessage(message)
            }
        }
    }

    private fun getVideoDuration(path: String): LongArray {
        val data = longArrayOf(0, 0, 0)
        val file = File(path)
        if (file.exists()) {
            val mediaPlayer = MediaPlayer()
            return try {
                mediaPlayer.setDataSource(file.path)
                mediaPlayer.prepare()
                data[1] = mediaPlayer.videoWidth.toLong()
                data[2] = mediaPlayer.videoHeight.toLong()
                data[0] = mediaPlayer.duration.toLong()
                mediaPlayer.release()
                data
            } catch (e: IOException) {
                e.printStackTrace()
                data
            }
        }
        return data
    }

    private fun judeIsCancelRecord(view: View, motionEvent: MotionEvent):Boolean{
        var location:IntArray = intArrayOf(0, 0)
        view.getLocationOnScreen(location)
        Log.d(TAG, "judeIsCancelRecord: ${location[0]} \t ${location[1]}")
        return motionEvent.rawY < location[1] - 40
        return false
    }
    private fun endRecord(cancel: Boolean){
        started = false
        activity.window.setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        audioRecorder?.completeRecord(cancel)
        stopRecord()
    }

    private fun cancelRecord(cancel: Boolean){
        Log.d(TAG, "cancelRecord: $cancel")
        if (!started){
            return
        }
        if (isCancelRecordAudio == cancel){
            return
        }
        isCancelRecordAudio = cancel
        updateView(cancel)
    }

    private fun startRecord(){
        recordLine.visibility = View.VISIBLE
        timer.base = SystemClock.elapsedRealtime()
        timer.start()
    }

    private fun stopRecord(){
        recordLine.visibility = View.GONE
        timer.stop()
        timer.base = SystemClock.elapsedRealtime()
    }
    private fun updateView(cancel: Boolean){
        Log.d(TAG, "updateView: $cancel")
        if (cancel){
            recordTips.text = activity.resources.getString(R.string.released_cancel)
            recordTips.setTextColor(Color.RED)
        }else{
            recordTips.text = activity.resources.getString(R.string.up_slide_cancel)
            recordTips.setTextColor(Color.WHITE)
        }
    }


    override fun onRecordSuccess(audioFile: File?, audioLength: Long, recordType: RecordType?) {
        if (!isCancelRecordAudio){
            //发送语音消息
            var voiceMessage = MessageBuilder.createAudioMessage(inputPanelInterface.getToAccIds(),sessionTypeEnum,audioFile,audioLength)
            voiceMessage.remoteExtension = messageRemoteMap
            inputPanelInterface.sendVoiceMessage(voiceMessage)
        }
    }

    override fun onRecordReachedMaxTime(maxTime: Int) {
        stopRecord()
        audioRecorder?.handleEndRecord(true, maxTime)
    }

    override fun onRecordReady() {

    }

    override fun onRecordCancel() {

    }

    override fun onRecordStart(audioFile: File?, recordType: RecordType?) {
        started = true
        if (!touched){
            return
        }
        updateView(false)
        startRecord()
    }

    override fun onRecordFail() {
        if (started){
            activity.resources.getString(R.string.record_error).showToast()
            stopRecord()
        }
    }


}