package com.yb.livechatkt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.Row
import com.yb.livechatkt.databinding.FragmentLiveBinding
import com.yb.livechatkt.ui.activity.LivePlayActivity
import com.yb.livechatkt.ui.adapter.HomeLiveRecyclerAdapter
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.vm.LiveViewModel

class LiveFragment : BaseFragment() {

    private val viewModel:LiveViewModel by viewModels<LiveViewModel>()
    private lateinit var binding: FragmentLiveBinding
    private val TAG = this.javaClass.simpleName
    private lateinit var adapter: HomeLiveRecyclerAdapter
    private var rowDatas:MutableList<Row> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_live,container,false)
        binding.data = viewModel
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }

    private fun initView(){
        adapter = HomeLiveRecyclerAdapter(requireContext(),rowDatas!!)
        var manager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.liveRecycler.layoutManager = manager
        binding.liveRecycler.adapter = adapter;
        viewModel.homeLiveLiveData.observe(viewLifecycleOwner, Observer {
            this.rowDatas.clear()
            this.rowDatas.addAll(it.page.rows)
            adapter.notifyDataSetChanged()
        })
        viewModel.getHomeLive()
        viewModel.tokenFailed.observe(viewLifecycleOwner, Observer {
            tokenFailed(it)
        })
        viewModel.wyLoginMonitor.observe(viewLifecycleOwner, Observer {
            wyLoginFailed(it)
        })

        adapter.onItemClickListener = object : HomeLiveRecyclerAdapter.OnItemClickListener{
            override fun itemClick(view: View, row: Row, position: Int) {
                var intent = Intent(requireContext(),LivePlayActivity::class.java)
                intent.putExtra(NetConstant.LIVE_ID,row.id)
                startActivity(intent)
            }

        }

    }




}