package com.yb.livechatkt.util

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import android.widget.Toast
import com.yb.livechatkt.LiveChatKtApplication
import java.util.*

fun String.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(LiveChatKtApplication.context,this,duration).show()
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