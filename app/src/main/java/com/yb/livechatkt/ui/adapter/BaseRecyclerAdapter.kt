package com.yb.livechatkt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.ParameterizedType

open abstract class BaseRecyclerAdapter<DB : ViewDataBinding, T>(var context:Context, var dataList:List<T>) : RecyclerView.Adapter<BaseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz = type.actualTypeArguments[0] as Class<DB>
        val method = clazz.getMethod("inflate", LayoutInflater::class.java)
        var vb = method.invoke(null, LayoutInflater.from(context)) as DB
        vb.root.layoutParams = parent.layoutParams
        return BaseViewHolder(vb, vb.root)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemViewClickLisenter?.let { it(position) }
        }
        holder.itemView.setOnLongClickListener {
            itemViewLongClickLisenter?.let {
                it1 -> it1(position)
            }
            true
        }
        initView(holder,dataList[position],position)
    }

    abstract fun initView(holder: BaseViewHolder,t: T,position: Int)

    override fun getItemCount(): Int = dataList.size


    private var itemViewClickLisenter:((Int) -> Unit )? = null
    private var itemViewLongClickLisenter:((Int) -> Unit )? = null


    fun itemViewClickListener(itemClick:(Int) -> Unit): Unit {
        this.itemViewClickLisenter = itemClick
    }

    fun itemViewLongClickListener(itemClick:(Int) -> Unit): Unit {
        this.itemViewLongClickLisenter = itemClick
    }

}