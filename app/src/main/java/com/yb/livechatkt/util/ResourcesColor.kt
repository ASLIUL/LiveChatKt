package com.yb.livechatkt.util

import android.content.Context
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

object ResourcesColor {

    fun changeSVGColo(
        context: Context,
        imageView: ImageView,
        color: Int,
        imageResId: Int
    ) {
        val vectorDrawableCompat =
            VectorDrawableCompat.create(context.resources, imageResId, context.theme)
        //你需要改变的颜色
        try {
            vectorDrawableCompat!!.setTint(color)
            imageView.setImageDrawable(vectorDrawableCompat)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}