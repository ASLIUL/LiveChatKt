package com.yb.livechatkt.view


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.RoomGiftMessage
import com.yb.livechatkt.databinding.LiveTestGiftShowLayoutBinding
import com.yb.livechatkt.ui.adapter.GiftShowListViewAdapter


/**
 * create by liu
 * on 2020/6/9 2:14 PM
 */
class GiftGridView(context: Context,attributeSet: AttributeSet) : LinearLayout(context,attributeSet) {

    private val TAG = "GiftGridView"

    private var giftBeans: List<RoomGiftMessage>? = null
    private var adapter: GiftShowListViewAdapter? = null
    private var isFirst = true
    private val binding:LiveTestGiftShowLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.live_test_gift_show_layout,null,false)

    init {
        addView(binding.root)
        binding.gridView.layoutManager = LinearLayoutManager(context).also { it.orientation = RecyclerView.VERTICAL }

    }

    fun setGiftBeans(giftBeans: List<RoomGiftMessage>) {
        this.giftBeans = giftBeans
        showData()
    }
    fun setGiftBeans(giftBeans: RoomGiftMessage) {
        this.giftBeans = arrayListOf(giftBeans)
        showData()
    }

    private fun showData() {
        if (giftBeans!!.isEmpty() || giftBeans!!.isEmpty()) {
            Log.d(TAG, "showData: 数据为空")
            return
        }
        if (isFirst) {
            adapter = GiftShowListViewAdapter(context, giftBeans!!)
            binding.gridView.adapter = adapter
            isFirst = false
        } else {
            adapter?.notifyDataSetChanged()
        }
    }

}