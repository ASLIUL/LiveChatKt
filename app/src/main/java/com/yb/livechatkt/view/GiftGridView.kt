package com.yb.livechatkt.view


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.GridView
import android.widget.LinearLayout
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.RoomGiftMessage
import com.yb.livechatkt.ui.adapter.GiftShowListViewAdapter


/**
 * create by liu
 * on 2020/6/9 2:14 PM
 */
class GiftGridView(context: Context,attributeSet: AttributeSet) : LinearLayout(context,attributeSet) {

    private val TAG = "GiftGridView"

    private var giftBeans: List<RoomGiftMessage>? = null
    private var view: View? = null
    private var gridView: GridView? = null
    private var adapter: GiftShowListViewAdapter? = null
    private var isFirst = true

    init {
        initView()
    }


    private fun initView() {
        view = LayoutInflater.from(context).inflate(R.layout.live_test_gift_show_layout, null, false)
        gridView = view!!.findViewById(R.id.grid_view)
        addView(view)
    }

    fun setGiftBeans(giftBeans: List<RoomGiftMessage>) {
        this.giftBeans = giftBeans
        showData()
    }

    private fun showData() {
        if (giftBeans!!.isEmpty() || giftBeans!!.isEmpty()) {
            Log.d(TAG, "showData: 数据为空")
            return
        }
        if (isFirst) {
            adapter = GiftShowListViewAdapter(context, giftBeans!!)
            gridView?.adapter = adapter
            isFirst = false
        } else {
            adapter?.notifyDataSetChanged()
        }
    }

}