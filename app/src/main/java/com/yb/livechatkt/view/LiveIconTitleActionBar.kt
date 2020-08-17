package com.yb.livechatkt.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.yb.livechatkt.R

class LiveIconTitleActionBar(context: Context,attributeSet: AttributeSet) : RelativeLayout(context,attributeSet) {

    //view
    var title:TextView
    var icon:ImageView
    var dic:ImageView

    //value
    var titleValue:String? = ""
    var iconValue:Int = R.mipmap.ic_launcher
    var dicVlaue:Int = R.drawable.ic_right_action_black


    init {

        val typedArray = context.obtainStyledAttributes(attributeSet,R.styleable.LiveIconTitleActionBar)
        titleValue = typedArray.getString(R.styleable.LiveIconTitleActionBar_title)
        iconValue = typedArray.getResourceId(R.styleable.LiveIconTitleActionBar_icon,R.mipmap.ic_launcher)
        dicVlaue = typedArray.getResourceId(R.styleable.LiveIconTitleActionBar_dic,R.drawable.ic_right_action_black)
        typedArray.recycle()

        val  view = View.inflate(context,R.layout.view_live_icon_title_bar_layout,this)
        title = view.findViewById(R.id.title);
        icon = view.findViewById(R.id.icon);
        dic = view.findViewById(R.id.dic);

        title.text = if (TextUtils.isEmpty(titleValue)) resources.getString(R.string.app_name) else titleValue

        icon.setImageDrawable(resources.getDrawable(iconValue,context.theme))

        dic.setImageDrawable(resources.getDrawable(dicVlaue,context.theme))
    }


}