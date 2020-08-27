package com.yb.livechatkt.util

import android.app.Activity
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity

class SoftKeyBoardListener(activity:Activity) {
    private val rootView = activity.window.decorView
    private var rootViewVisibleHeight:Int = 0

    init {
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            //获取当前根视图显示的大小
            var r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val visibleHeight = r.height()
            if (visibleHeight == 0){
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }
            //根据视图高度没有变化，可以看作软键盘的状态没有变化
            if (rootViewVisibleHeight ==  visibleHeight){
                return@addOnGlobalLayoutListener
            }
            //看作显示
            if (rootViewVisibleHeight - visibleHeight >200){
                onSoftKeyBoardChangeListener?.keyBoardShow(rootViewVisibleHeight - visibleHeight)
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }
            if (visibleHeight - rootViewVisibleHeight > 200){
                onSoftKeyBoardChangeListener?.keyBoardHide(visibleHeight - rootViewVisibleHeight)
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

        }
    }

    interface OnSoftKeyBoardChangeListener{
        fun keyBoardShow(height:Int)
        fun keyBoardHide(height:Int)
    }
    var onSoftKeyBoardChangeListener:OnSoftKeyBoardChangeListener? = null

}