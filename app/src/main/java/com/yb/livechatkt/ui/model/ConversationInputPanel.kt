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
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.netease.nimlib.sdk.media.record.AudioRecorder
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback
import com.netease.nimlib.sdk.media.record.RecordType
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.permissionx.guolindev.PermissionX
import com.yb.livechatkt.R
import com.yb.livechatkt.util.showToast
import java.io.File
import java.io.IOException

class ConversationInputPanel(
    private var activity: AppCompatActivity,
    rootView: View,
    val sessionTypeEnum: SessionTypeEnum,
    private var inputPanelInterface: ConversationInputPanelInterface
) : IAudioRecordCallback {

    var timer: Chronometer = rootView.findViewById(R.id.recording_time)
    var recordTips: TextView = rootView.findViewById(R.id.record_tips)
    var recordLine: LinearLayout = rootView.findViewById(R.id.recording_line)
    var moreAction: ImageView = rootView.findViewById(R.id.more_action)
    var ActionRecycler: RecyclerView = rootView.findViewById(R.id.action_bar)
    var msgConnect:EditText = rootView.findViewById(R.id.connect)
    var send:TextView = rootView.findViewById(R.id.send)
    val touchRecord:TextView = rootView.findViewById(R.id.touch_recording)
    val voice:ImageView = rootView.findViewById(R.id.voice)
    private val TAG = "ConversationInputPanel"

    //1 图片 2语音，3视频
    private var messageType:Int = 1
    //是否开始录音
    private var startRecording:Boolean = false
    //是否正在录音
    private var isRecording:Boolean = false
    //是否取消录音
    private var isCancelRecord:Boolean = true

    var audioRecorder: AudioRecorder? = null
    //录音最长时间
    val maxTime:Int = 120

    private val picCode:Int = 100001
    private val videoCode:Int = 100002
    private val cameraCode:Int = 100004

    init {
        send.setOnClickListener {
            sendTextMessage()
        }
        touchRecord.setOnTouchListener { view, motionEvent -> {

        }
            PermissionX.init(activity).permissions(Manifest.permission.RECORD_AUDIO)
                .request{ allGranted, grantedList, deniedList ->
                    if (allGranted){
                        initRecord(view, motionEvent)
                    }
                }

            false
        }
        voice.setOnClickListener {
            if (touchRecord.visibility == View.VISIBLE){
                touchRecord.visibility = View.GONE
                msgConnect.visibility = View.VISIBLE
                send.visibility = View.VISIBLE
            }else{
                touchRecord.visibility = View.VISIBLE
                msgConnect.visibility = View.GONE
                send.visibility = View.GONE
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
        msgConnect.setText("")
        inputPanelInterface.sendTextMessage(textMessage)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent){
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
                inputPanelInterface.sendImageMessage(message)
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

    private fun initRecord(view: View, event: MotionEvent){
        Log.d(TAG, "initRecord: ${event.action}")
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                isCancelRecord = false
                audioRecorder = AudioRecorder(activity, RecordType.AAC, maxTime, this)
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                )
                audioRecorder?.startRecord()
                isRecording = true
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                isRecording = false
                endRecord(judeIsCancelRecord(view, event))
            }
            MotionEvent.ACTION_MOVE -> {
                isRecording = true
                cancelRecord(judeIsCancelRecord(view, event))
            }

        }
    }
    private fun judeIsCancelRecord(view: View, motionEvent: MotionEvent):Boolean{
        var location:IntArray = intArrayOf(1, 2)
        view.getLocationOnScreen(location)
        return if (motionEvent.rawY < location[1] + 40){
            updateView(true)
            isCancelRecord = true
            true
        }else{
            updateView(false)
            isCancelRecord = false
            false
        }
    }
    private fun endRecord(cancel: Boolean){
       startRecording = false
        activity.window.setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        audioRecorder?.completeRecord(cancel)
        stopRecord()
    }

    private fun cancelRecord(cancel: Boolean){
        Log.d(TAG, "cancelRecord: $cancel")
        if (!startRecording){
            return
        }
        if (isCancelRecord == cancel){
            return
        }
        isCancelRecord = cancel
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
        if (!isCancelRecord){
            //发送语音消息
            var voiceMessage = MessageBuilder.createAudioMessage(inputPanelInterface.getToAccIds(),sessionTypeEnum,audioFile,audioLength)
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
        startRecording = true
        if (!isRecording){
            return
        }
        updateView(false)
        startRecord()
    }

    override fun onRecordFail() {
        if (startRecording){
            activity.resources.getString(R.string.record_error).showToast()
            stopRecord()
        }
    }


}