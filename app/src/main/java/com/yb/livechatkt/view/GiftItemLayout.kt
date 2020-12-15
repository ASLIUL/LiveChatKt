package com.yb.livechatkt.view


import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.RoomGiftMessage
import com.yb.livechatkt.databinding.ViewItemGiftBinding
import com.yb.livechatkt.net.LiveChatUrl


class GiftItemLayout(context: Context,attributeSet: AttributeSet) : LinearLayout(context,attributeSet), Animation.AnimationListener {
    val TAG = GiftItemLayout::class.java.simpleName

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    removeCallbacksAndMessages(null)
                    state = GIFTITEM_DEFAULT
                    if (animListener == null) return
                    animListener?.giftAnimEnd(index)
                }
            }
        }
    }

    /**
     * 当前显示状态
     */
    var state = GIFTITEM_DEFAULT
    /**
     * 当前显示位置
     */
    var index = 0
    /**
     * 当前tag
     */
    private var myTag: String? = null
    private var giftMessage:RoomGiftMessage? = null
    /**
     * 透明度动画(200ms), 连击动画(200ms)
     */
    private var translateAnim: Animation? = null
    private var numAnim: Animation? = null
    private var animListener: GiftAnimListener? = null
    private var binding: ViewItemGiftBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.view_item_gift,this,false)

    /**
     * 初始化
     */
    init {
        addView(binding.root)
        initTranslateAnim()
        initNumAnim()
        val typed = context.obtainStyledAttributes(attributeSet, R.styleable.GiftItemLayout, 0, 0)
        index = typed.getInteger(R.styleable.GiftItemLayout_gift_index, 0)
        typed.recycle()
    }

    /**
     * 设置礼物item显示的数据
     * @param giftBean
     */
    fun setData(giftMessage: RoomGiftMessage) {
        visibility = VISIBLE
        this.giftMessage = giftMessage
        myTag = giftMessage.username + giftMessage.giftname
        Glide.with(context).load(LiveChatUrl.imgBaseUrl+giftMessage.chatAccount).into(binding.crvheadimage)
        binding.tvUserName.text = giftMessage.username
        binding.tvMessage.text = giftMessage.giftname
        binding.giftNum.text = "x${giftMessage.giftnum}"
        Glide.with(context).asGif().load(giftMessage.giftimg).into(binding.ivgift)
    }

    /**
     * 执行了礼物数量连接效果
     * @param sortNum
     */
    fun addCount(sortNum: Int) {
        handler.removeMessages(0)
        giftMessage?.giftnum = giftMessage?.giftnum?.plus(sortNum)
        binding.giftNum.text = "x${giftMessage?.giftnum}"
        binding.giftNum.startAnimation(numAnim) // 执行礼物数量动画
    }

    fun startAnimation() {
        binding.crvheadimage.startAnimation(translateAnim)
        state = GIFTITEM_SHOW
    }

    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {
        if (animation === translateAnim) { // 头像渐变动画执行完毕
            binding.crvheadimage.clearAnimation()
            binding.giftNum.startAnimation(numAnim) // 执行礼物数量动画
        } else {
            handler.sendEmptyMessageDelayed(0, SHOW_TIME.toLong())
        }
    }

    override fun onAnimationRepeat(animation: Animation) {}

    /**
     * 初始化位移动画
     */
    private fun initTranslateAnim() {
        translateAnim = TranslateAnimation(-300f, 0f, 0f, 0f)
        translateAnim?.duration = 200
        translateAnim?.fillAfter = true
        translateAnim?.setAnimationListener(this)
    }

    /**
     * 初始化礼物数字动画
     */
    private fun initNumAnim() {
        numAnim = ScaleAnimation(
            0.5f,
            1.0f,
            0.5f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        numAnim?.duration = 200
        numAnim?.setAnimationListener(this)
    }

    fun getAnimListener(): GiftAnimListener? {
        return animListener
    }

    fun setAnimListener(animListener: GiftAnimListener?) {
        this.animListener = animListener
    }

    companion object {
        const val SHOW_TIME = 1500
        const val GIFTITEM_DEFAULT = 0
        const val GIFTITEM_SHOW = 1
    }
}