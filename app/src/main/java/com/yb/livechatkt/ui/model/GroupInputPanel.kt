package com.yb.livechatkt.ui.model

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
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
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.netease.nimlib.sdk.media.record.AudioRecorder
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback
import com.netease.nimlib.sdk.media.record.RecordType
import com.permissionx.guolindev.PermissionX
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.ActionType
import com.yb.livechatkt.bean.GroupMessageBean
import com.yb.livechatkt.bean.PlugBean
import com.yb.livechatkt.ui.adapter.PlugRecyclerAdapter
import com.yb.livechatkt.util.showToast
import com.yb.livechatkt.util.uriToFileQ
import java.io.File

class GroupInputPanel(private var activity: AppCompatActivity, rootView: View, private var groupMSgInputPanelInterface: GroupMSgInputPanelInterface) : IAudioRecordCallback {

    var timer: Chronometer = rootView.findViewById(R.id.recording_time)
    var recordTips: TextView = rootView.findViewById(R.id.record_tips)
    var recordLine: LinearLayout = rootView.findViewById(R.id.recording_line)
    var moreAction: ImageView = rootView.findViewById(R.id.more_action)
    var moreActionLine: LinearLayout = rootView.findViewById(R.id.more_action_line)
    var actionRecycler: RecyclerView = rootView.findViewById(R.id.action_bar)
    var msgConnect:EditText = rootView.findViewById(R.id.connect)
    var send:TextView = rootView.findViewById(R.id.send)
    val touchRecord:TextView = rootView.findViewById(R.id.touch_recording)
    val voice:ImageView = rootView.findViewById(R.id.voice)
    private val TAG = "InputPanel"

    var plugRecyclerAdapter : PlugRecyclerAdapter? = null


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
                .request{allGranted, grantedList, deniedList ->
                    if (allGranted){
                        initRecord(view,motionEvent)
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
        moreAction.setOnClickListener {
            if (groupMSgInputPanelInterface.getKeyBoardIsShow()){
                groupMSgInputPanelInterface.hideKeyBoard()
            }
            if (moreActionLine.visibility == View.VISIBLE) moreActionLine.visibility = View.GONE else moreActionLine.visibility = View.VISIBLE
        }

        plugRecyclerAdapter?.onItemClickListener = object : PlugRecyclerAdapter.OnItemClickListener{
            override fun itemClick(view: View, plugBean: PlugBean, position: Int) {
                when(plugBean.action){
                    ActionType.ALUBM ->{
                        messageType = 1
                        PictureSelector
                            .create(activity)
                            .openGallery(PictureMimeType.ofImage())
                            .selectionMode(PictureConfig.SINGLE)
                            .isGif(false)
                            .cropImageWideHigh(1,1)
                            .freeStyleCropEnabled(true)
                            .scaleEnabled(true)
                            .queryMaxFileSize(10f)
                            .isReturnEmpty(true)
                            .imageEngine(PictureSelectEngine())
                            .forResult(object : OnResultCallbackListener<LocalMedia> {
                                override fun onResult(result: MutableList<LocalMedia>?) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        createFileMessage(uriToFileQ(activity, Uri.parse(result?.get(0)!!.path))?.absolutePath!!)
                                    }else{
                                        createFileMessage(result?.get(0)!!.path)
                                    }
                                }

                                override fun onCancel() {

                                }

                            })

                    }
                    ActionType.VIDEO ->{
                        messageType = 3
                        PictureSelector
                            .create(activity)
                            .openGallery(PictureMimeType.ofVideo())
                            .selectionMode(PictureConfig.SINGLE)
                            .isGif(false)
                            .cropImageWideHigh(1,1)
                            .freeStyleCropEnabled(true)
                            .scaleEnabled(true)
                            .queryMaxFileSize(10f)
                            .isReturnEmpty(true)
                            .imageEngine(PictureSelectEngine())
                            .forResult(object : OnResultCallbackListener<LocalMedia> {
                                override fun onResult(result: MutableList<LocalMedia>?) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        createFileMessage(uriToFileQ(activity, Uri.parse(result?.get(0)!!.path))?.absolutePath!!)
                                    }else{
                                        createFileMessage(result?.get(0)!!.path)
                                    }
                                }

                                override fun onCancel() {

                                }

                            })

                    }
                    }
                }
            }

    }


    private fun sendTextMessage(){
        var bean = GroupMessageBean.createTextMessage(msgConnect.text.toString(),groupMSgInputPanelInterface.getToAccIds())
        groupMSgInputPanelInterface.sendTextMessage(bean)
    }

    fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent){
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
        createFileMessage(resultDatas[0])
    }

    private fun createFileMessage(fileDatas:String){
        when(messageType){
            1->{
                val imageMessage = GroupMessageBean.createPicMessage(File(fileDatas),"",groupMSgInputPanelInterface.getToAccIds())
                groupMSgInputPanelInterface.sendImageMessage(imageMessage)
            }
            3 ->{
                val videoMessage = GroupMessageBean.createVideoMessage(File(fileDatas),"",groupMSgInputPanelInterface.getToAccIds())
                groupMSgInputPanelInterface.sendVideoMessage(videoMessage)
            }
        }
    }

    private fun initRecord(view: View,event:MotionEvent){
        Log.d(TAG, "initRecord: ${event.action}")
        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                isCancelRecord = false
                audioRecorder = AudioRecorder(activity,RecordType.AAC,maxTime,this)
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                audioRecorder?.startRecord()
                isRecording = true
            }
            MotionEvent.ACTION_CANCEL,MotionEvent.ACTION_UP ->{
                isRecording = false
                endRecord(judeIsCancelRecord(view,event))
            }
            MotionEvent.ACTION_MOVE -> {
                isRecording = true
                cancelRecord(judeIsCancelRecord(view,event))
            }

        }
    }
    private fun judeIsCancelRecord(view: View,motionEvent: MotionEvent):Boolean{
        var location:IntArray = intArrayOf(1,2)
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
        activity.window.setFlags(0,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
    private fun updateView(cancel:Boolean){
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
            val bean = GroupMessageBean.createVoiceMessage(audioFile!!,"",audioLength,groupMSgInputPanelInterface.getToAccIds())
            groupMSgInputPanelInterface.sendVoiceMessage(bean)
        }
    }

    override fun onRecordReachedMaxTime(maxTime: Int) {
        stopRecord()
        audioRecorder?.handleEndRecord(true,maxTime)
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
            stopRecord()
        }
    }


}