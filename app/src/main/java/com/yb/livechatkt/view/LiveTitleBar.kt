package com.yb.livechatkt.view

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.yb.livechatkt.R
import com.yb.livechatkt.util.ResourcesColor

class LiveTitleBar(context: Context,attributeSet: AttributeSet) : ConstraintLayout(context,attributeSet) {

    //view
    var leftTitle :TextView
    var leftImg:ImageView
    var centerTitle:TextView
    var rightTitle:TextView
    var rightImg:ImageView

    //value
    var leftTitleValue:String?
    var leftImgValue:Int = R.drawable.ic_baseline_arrow_back_ios_24
    var leftType:Int = 0
    var centerTitleValue:String?
    var centerType:Int = 0
    var rightTitleValue:String?
    var rightImgValue:Int = R.drawable.ic_menu_black_24dp
    var rightType:Int = 0
    var textColor:Int = Color.BLACK



    init {

        val typedArray = context.obtainStyledAttributes(attributeSet,R.styleable.LiveTitleBar)
        leftType = typedArray.getInt(R.styleable.LiveTitleBar_leftType,0)
        leftTitleValue = typedArray.getString(R.styleable.LiveTitleBar_leftTitle)
        leftImgValue = typedArray.getResourceId(R.styleable.LiveTitleBar_leftImg,R.drawable.ic_baseline_arrow_back_ios_24)
        centerTitleValue = typedArray.getString(R.styleable.LiveTitleBar_centerTitle)
        centerType = typedArray.getInt(R.styleable.LiveTitleBar_centerType,0)
        rightTitleValue = typedArray.getString(R.styleable.LiveTitleBar_rightTitle)
        rightImgValue = typedArray.getResourceId(R.styleable.LiveTitleBar_rightImg,R.drawable.ic_menu_black_24dp)
        rightType = typedArray.getInt(R.styleable.LiveTitleBar_rightType,0)
        textColor = typedArray.getColor(R.styleable.LiveTitleBar_textColor,Color.BLACK)
        typedArray.recycle()

        val view:View = View.inflate(context,R.layout.view_live_title_bar_layout,this)
        leftTitle = view.findViewById(R.id.left_title)
        leftImg = view.findViewById(R.id.left_img)
        centerTitle = view.findViewById(R.id.center_title)
        rightImg = view.findViewById(R.id.right_img)
        rightTitle = view.findViewById(R.id.right_title)
        initViewData()
    }

    private fun initViewData(){
        // left
        when(leftType){
            1 -> {
                leftTitle.visibility = View.VISIBLE
                leftImg.visibility = View.GONE
                leftTitle.text = if (TextUtils.isEmpty(leftTitleValue)) resources.getText(R.string.back) else leftTitleValue
                leftTitle.setTextColor(textColor)
            }
            2 -> {
                leftImg.visibility = View.VISIBLE
                leftTitle.visibility = View.GONE
                //leftImg.setImageDrawable(resources.getDrawable(leftImgValue,context.theme))
                ResourcesColor.changeSVGColo(context,leftImg,textColor,leftImgValue)
            }
            3 -> {
                leftImg.visibility = View.VISIBLE
                leftTitle.visibility = View.VISIBLE
                leftTitle.text = if (TextUtils.isEmpty(leftTitleValue)) resources.getText(R.string.back) else leftTitleValue
                leftTitle.setTextColor(textColor)
                ResourcesColor.changeSVGColo(context,leftImg,textColor,leftImgValue)
            }
            else -> {
                leftImg.visibility = View.GONE
                leftTitle.visibility = View.GONE
            }
        }

        //center
        if (centerType == 1) {centerTitle.visibility = View.VISIBLE;centerTitle.setTextColor(textColor);centerTitle.text = if (TextUtils.isEmpty(centerTitleValue)) resources.getText(R.string.app_name) else centerTitleValue} else centerTitle.visibility = View.GONE

        //right
        when(rightType){
            1 ->{
                rightTitle.visibility = View.VISIBLE
                rightImg.visibility = View.GONE
                rightTitle.text = if (TextUtils.isEmpty(rightTitleValue)) resources.getText(R.string.menu) else rightTitleValue
                rightTitle.setTextColor(textColor)
            }
            2 -> {
                rightTitle.visibility = View.GONE
                rightImg.visibility = View.VISIBLE
                ResourcesColor.changeSVGColo(context,rightImg,textColor,rightImgValue)
            }
            3 -> {
                rightTitle.visibility = View.VISIBLE
                rightImg.visibility = View.VISIBLE
                rightTitle.text = if (TextUtils.isEmpty(rightTitleValue)) resources.getText(R.string.menu) else rightTitleValue
                ResourcesColor.changeSVGColo(context,rightImg,textColor,rightImgValue)
                rightTitle.setTextColor(textColor)
            }
            else -> {
                rightImg.visibility = View.GONE
                rightTitle.visibility = View.GONE
            }
        }

    }



}