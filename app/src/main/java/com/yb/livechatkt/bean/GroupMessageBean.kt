package com.yb.livechatkt.bean

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.google.gson.Gson
import java.io.File

class GroupMessageBean(var uuid:String,var body:String,var file:String,var toAccids:String,var type:Int,var sendState:String,var filepPath:String) {

    companion object{
        fun createTextMessage(text:String,toAccids: String):GroupMessageBean {
            return GroupMessageBean(System.currentTimeMillis().toString(),Gson().toJson(TextMessage(text)),"",toAccids,0,"","")
        }
        fun createPicMessage(file:File,url:String,toAccids: String):GroupMessageBean {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            return GroupMessageBean(
                System.currentTimeMillis().toString(),
                Gson().toJson(
                    PicMessage(
                        file.name,
                        getExt(file.absolutePath),
                        bitmap.width,
                        bitmap.height,
                        file.length()
                    )
                ),
                url,
                toAccids,
                1,
                "",
                file.absolutePath)
        }
        fun createVoiceMessage(file:File,url:String,audioLength:Long,toAccids: String):GroupMessageBean {
            return GroupMessageBean(
                System.currentTimeMillis().toString(),
                Gson().toJson(
                VoiceMessage(
                    audioLength,
                    "aac",
                    file.length()
                )
            ),
                url,
                toAccids,
                2,
            "",filepPath = file.absolutePath)
        }
        fun createVideoMessage(file:File,url:String,toAccids: String):GroupMessageBean {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(file.absolutePath)
            return GroupMessageBean(
                System.currentTimeMillis().toString(),
                Gson().toJson(
                VideoMessage(
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()!!,
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()!!,
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()!!,
                    getExt(file.absolutePath),
                    file.length()
                )
            ),
                url,
                toAccids,
                3,
                "",file.absolutePath)
        }



        private fun getExt(name:String):String{
            return name.substring(name.lastIndexOf(".")+1)
        }
    }

}