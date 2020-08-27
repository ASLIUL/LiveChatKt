package com.yb.livechatkt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.BannerDataBean
import com.yb.livechatkt.bean.Row
import com.yb.livechatkt.databinding.FragmentLiveBinding
import com.yb.livechatkt.ui.activity.LivePlayActivity
import com.yb.livechatkt.ui.adapter.HomeLiveRecyclerAdapter
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.vm.LiveViewModel
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

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
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }

    private fun initView(){
        val data = BannerDataBean.getData()
        binding.banner.adapter = object :BannerImageAdapter<BannerDataBean>(BannerDataBean.getData()){
            override fun onBindView(
                holder: BannerImageHolder?,
                data: BannerDataBean?,
                position: Int,
                size: Int
            ) {
                Glide.with(requireContext()).load(data?.imageRes).apply(RequestOptions.bitmapTransform(RoundedCorners(30))).into(holder?.imageView!!)
            }
        }

        adapter = HomeLiveRecyclerAdapter(requireContext(),rowDatas!!)
        var manager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.liveRecycler.layoutManager = manager
        binding.liveRecycler.adapter = adapter;
        viewModel.homeLiveLiveData?.observe(viewLifecycleOwner, Observer {
            this.rowDatas.clear()
            if (it.page.rows.isEmpty()){
                binding.isHasLive.visibility = View.VISIBLE
                binding.liveRecycler.visibility = View.GONE
            }else{
                binding.isHasLive.visibility = View.GONE
                binding.liveRecycler.visibility = View.VISIBLE
                this.rowDatas.addAll(it.page.rows)
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.getHomeLive()
        viewModel.tokenFailed?.observe(viewLifecycleOwner, Observer {
            tokenFailed(it)
        })
        viewModel.wyLoginMonitor?.observe(viewLifecycleOwner, Observer {
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