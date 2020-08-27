package com.yb.livechatkt.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.yb.livechatkt.LiveChatKtApplication
import com.yb.livechatkt.R
import com.yb.livechatkt.ui.model.LiveCustomAttachment
import com.yb.livechatkt.ui.model.ShareLiveMessageAttachment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

fun String.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(LiveChatKtApplication.context, this, duration).show()
}
fun getBitmapFormUrl(url: String?): Bitmap? {
    var bitmap: Bitmap? = null
    val retriever = MediaMetadataRetriever()
    try {
        if (Build.VERSION.SDK_INT >= 14) {
            retriever.setDataSource(url, HashMap())
        } else {
            retriever.setDataSource(url)
        }
        /*getFrameAtTime()--->在setDataSource()之后调用此方法。 如果可能，该方法在任何时间位置找到代表性的帧，
         并将其作为位图返回。这对于生成输入数据源的缩略图很有用。**/bitmap = retriever.frameAtTime
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } finally {
        try {
            retriever.release()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
    return bitmap
}

//协程处理保存图片
suspend fun savePhoto(context: Context, bitmap: Bitmap){ //关键字suspend:在另外的线程允许挂起
    withContext(Dispatchers.IO) {
        //定义线程的范围
        //获取图像的位图资源
        //设置保存地址
        val saveUri: Uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        ) ?: kotlin.run {
            context.resources.getString(R.string.save_failed).showToast()
            return@withContext
        }

        //保存图片
        context.contentResolver.openOutputStream(saveUri).use {
            val  success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            withContext(Dispatchers.Main){
                if (success){
                context.resources.getString(R.string.save_success).showToast()
                }else{
                    context.resources.getString(R.string.save_failed).showToast()
                }
            }

        }
    }
}
private val displayMetrics = Resources.getSystem().displayMetrics
fun getScreenWidth():Int{
    return displayMetrics.widthPixels
}
fun getScreenHeight():Int{
    return displayMetrics.heightPixels
}

fun getConnect(imMessage: IMMessage):String{
    return when(imMessage.msgType){
        MsgTypeEnum.text -> imMessage.content
        MsgTypeEnum.video -> "[视频]"
        MsgTypeEnum.image -> "[图片]"
        MsgTypeEnum.audio -> "[语音]"
        MsgTypeEnum.custom -> "[分享信息]"
        else -> "未读消息"
    }
}

fun getConnect(message: RecentContact): String {
    if (message.msgType == MsgTypeEnum.text) {
        return message.content
    } else if (message.msgType == MsgTypeEnum.video) {
        return "[视频]"
    } else if (message.msgType == MsgTypeEnum.audio) {
        return "[语音]"
    } else if (message.msgType == MsgTypeEnum.image) {
        return "[图片]"
    } else if (message.msgType == MsgTypeEnum.tip) {
        return message.content
    } else if (message.msgType == MsgTypeEnum.custom) {
        val liveCustomAttachment = message.attachment as LiveCustomAttachment
        if (liveCustomAttachment is ShareLiveMessageAttachment) {
            return "分享了直播"
        }
    }
    return "收到一条新消息"
}


@RequiresApi(Build.VERSION_CODES.Q)
fun uriToFileQ(context: Context, uri: Uri): File? =
    if (uri.scheme == ContentResolver.SCHEME_FILE)
        uri.toFile()
    else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        //把文件保存到沙盒
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val ois = context.contentResolver.openInputStream(uri)
                val displayName =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                ois?.let {
                    File(
                        context.externalCacheDir!!.absolutePath,
                        "${Random().nextInt(9999)}$displayName"
                    ).apply {
                        val fos = FileOutputStream(this)
                        android.os.FileUtils.copy(ois, fos)
                        fos.close()
                        it.close()
                    }
                }
            } else null

        }

    } else null

fun dp2px(context: Context, dipValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

fun transToString(time: Long):String{
    return SimpleDateFormat("hh:mm").format(time)
}
fun transToTimeStamp(date: String):Long{
    return SimpleDateFormat("hh:mm").parse(date, ParsePosition(0)).time
}