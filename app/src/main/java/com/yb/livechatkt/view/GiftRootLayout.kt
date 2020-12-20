package com.yb.livechatkt.view


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.GiftItemData
import com.yb.livechatkt.bean.GiftAnimData
import java.util.*
import kotlin.collections.ArrayList


class GiftRootLayout(context: Context,private val attributeSet: AttributeSet?) : LinearLayout(context,attributeSet), Animation.AnimationListener, GiftAnimListener {


    private val TAG = GiftRootLayout::class.java.simpleName
    private var oneItemLayout: GiftItemLayout? = null
    private var twoItemLayout: GiftItemLayout? = null
    private var threadItemLayout: GiftItemLayout? = null
    private var oneGiftItemInAnim: Animation? = null
    private var oneGiftItemOutAnim: Animation? = null
    private var twoGiftItemInAnim: Animation? = null
    private var twoGiftItemOutAnim: Animation? = null
    private var threadGiftItemInAnim: Animation? = null
    private var threadGiftItemOutAnim: Animation? = null
    private var itemLayouts : MutableList<GiftItemLayout> = ArrayList()
    private val giftBeanTreeMap: TreeMap<Int, GiftAnimData> = TreeMap<Int, GiftAnimData>(Comparator<Int?> { o1, o2 -> o2.compareTo(o1) })

    /**
     * 初始化
     *
     * @param context
     */
    init {
        visibility = GONE
        oneGiftItemInAnim = AnimationUtils.loadAnimation(context, R.anim.gift_in)
        oneGiftItemInAnim?.fillAfter = true
        oneGiftItemOutAnim = AnimationUtils.loadAnimation(context, R.anim.gift_out)
        oneGiftItemOutAnim?.fillAfter = true
        twoGiftItemInAnim = AnimationUtils.loadAnimation(context, R.anim.gift_in)
        twoGiftItemInAnim?.fillAfter = true
        twoGiftItemOutAnim = AnimationUtils.loadAnimation(context, R.anim.gift_out)
        twoGiftItemOutAnim?.fillAfter = true
        threadGiftItemInAnim = AnimationUtils.loadAnimation(context, R.anim.gift_in)
        threadGiftItemInAnim?.fillAfter = true
        threadGiftItemOutAnim = AnimationUtils.loadAnimation(context, R.anim.gift_out)
        threadGiftItemOutAnim?.fillAfter = true
        oneGiftItemOutAnim?.setAnimationListener(this)
        twoGiftItemOutAnim?.setAnimationListener(this)
        threadGiftItemOutAnim?.setAnimationListener(this)
    }

    fun setItemLayout(itemLayouts: List<GiftItemLayout>) {
        if (itemLayouts.isNullOrEmpty()) return
        if (this.itemLayouts.isEmpty() || this.itemLayouts.size < 1) {
            this.itemLayouts.addAll(itemLayouts)
        } else {
            this.itemLayouts.clear()
            this.itemLayouts.addAll(itemLayouts)
        }

        oneItemLayout = itemLayouts[0]
        oneItemLayout!!.setAnimListener(this)
        twoItemLayout = itemLayouts[1]
        twoItemLayout!!.setAnimListener(this)
        threadItemLayout = itemLayouts[2]
        threadItemLayout!!.setAnimListener(this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (!changed || childCount == 0) return
    }

    fun loadGift(giftAnimData: GiftAnimData) {
        val tag: String = giftAnimData.username + giftAnimData.id
        if (oneItemLayout?.state == GiftItemLayout.GIFTITEM_SHOW && oneItemLayout?.myTag.equals(tag)) {
            oneItemLayout?.addCount(1)
            return
        }
        if (twoItemLayout?.state == GiftItemLayout.GIFTITEM_SHOW && twoItemLayout?.myTag.equals(tag)) {
            twoItemLayout?.addCount(1)
            return
        }
        if (threadItemLayout?.state == GiftItemLayout.GIFTITEM_SHOW && threadItemLayout?.myTag.equals(tag)) {
            threadItemLayout?.addCount(1)
            return
        }
        addGift(giftAnimData)
    }

    private fun addGift(giftMessage: GiftAnimData) {
        if (giftBeanTreeMap.isEmpty()) {
            giftBeanTreeMap[giftMessage.id] = giftMessage
            showGift()
            return
        }
        for (key in giftBeanTreeMap.keys) {
            val result: GiftAnimData? = giftBeanTreeMap[key]
            val tagNew: String = giftMessage.username + giftMessage.id
            val tagOld: String = result?.username + result?.id
            if (tagNew == tagOld) {
                giftMessage.num = result?.num?.plus(1)!!
                giftBeanTreeMap.remove(result.id)
                giftBeanTreeMap[giftMessage.id] = giftMessage
                return
            }
        }
        giftBeanTreeMap[giftMessage.id] = giftMessage
    }

    private fun showGift() {
        if (giftBeanTreeMap.isEmpty()) return
        visibility = VISIBLE
        when{
            oneItemLayout?.state == GiftItemLayout.GIFTITEM_DEFAULT -> {
                oneItemLayout?.setData(gift)
                oneItemLayout?.visibility = VISIBLE
                oneItemLayout?.startAnimation(oneGiftItemInAnim)
                oneItemLayout?.startAnimation()
            }
            twoItemLayout?.state == GiftItemLayout.GIFTITEM_DEFAULT -> {
                twoItemLayout?.setData(gift)
                twoItemLayout?.visibility = VISIBLE
                twoItemLayout?.startAnimation(twoGiftItemInAnim)
                twoItemLayout?.startAnimation()
            }
            threadItemLayout?.state == GiftItemLayout.GIFTITEM_DEFAULT -> {
                threadItemLayout?.setData(gift)
                threadItemLayout?.visibility = VISIBLE
                threadItemLayout?.startAnimation(threadGiftItemInAnim)
                threadItemLayout?.startAnimation()
            }
        }
    }

    // 获取队列首个礼物实体
    // 移除队列首个礼物实体
    private val gift: GiftAnimData?
        get() {
            var giftBean: GiftAnimData? = null
            if (giftBeanTreeMap!!.size != 0) {
                // 获取队列首个礼物实体
                giftBean = giftBeanTreeMap.firstEntry().value
                // 移除队列首个礼物实体
                giftBeanTreeMap.remove(giftBeanTreeMap.firstKey())
            }
            return giftBean
        }

    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {
        showGift()
    }

    override fun onAnimationRepeat(animation: Animation) {}
    override fun giftAnimEnd(position: Int) {
        when (position) {
            0 -> oneItemLayout?.startAnimation(oneGiftItemOutAnim)
            1 -> twoItemLayout?.startAnimation(twoGiftItemOutAnim)
            2 -> threadItemLayout?.startAnimation(threadGiftItemOutAnim)
        }
    }
}