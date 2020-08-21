package com.yb.livechatkt.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.Session
import com.yb.livechatkt.databinding.FragmentSessionBinding
import com.yb.livechatkt.db.DBManager
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.ui.adapter.SessionRecyclerAdapter
import com.yb.livechatkt.util.getConnect
import com.yb.livechatkt.vm.SessionViewModel

class SessionFragment : BaseFragment() {

    val viewModel by viewModels<SessionViewModel>()
    lateinit var binding:FragmentSessionBinding
    val sessionDao = DBManager.getDBInstance().getSessionDao()
    private var sessionAdapter:SessionRecyclerAdapter? = null
    private var sessionList:MutableList<Session> = ArrayList()
    private var sessionMap:MutableMap<String,Session>  = HashMap()
    private val TAG = "SessionFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session,container,false)
        initView()
        return binding.root
    }
    private fun initView(){

        var manager = LinearLayoutManager(requireContext())
        manager.orientation = RecyclerView.VERTICAL
        binding.sessionRecycler.layoutManager = manager
        sessionAdapter = SessionRecyclerAdapter(requireContext(),sessionList)
        binding.sessionRecycler.adapter = sessionAdapter

        viewModel.tokenFailed.observe(viewLifecycleOwner, Observer {
            tokenFailed(it)
        })
        viewModel.wyLoginMonitor.observe(viewLifecycleOwner, Observer {
            wyLoginFailed(it)
        })
        viewModel.receiveMessageLiveData.observe(requireActivity(), Observer {
            if (it.isNotEmpty()) {
                it.forEach { im ->
                    Log.d(TAG, "initView: ${getConnect(im)}")
                    if (sessionMap.containsKey(im.sessionId)) {
                        var session = sessionMap[im.sessionId]
                        session?.lastMsg = getConnect(im)
                        session?.lastMsgTime = im.time
                        session?.unRead = session?.unRead?.plus(1)!!
                        sessionMap[im.sessionId] = session
                    } else {
                        var session = sessionMap[im.sessionId]
                        session?.lastMsg = getConnect(im)
                        session?.lastMsgTime = im.time
                        session?.unRead = session?.unRead?.plus(1)!!
                        session?.header = LiveChatUrl.headerBaseUrl + im.sessionId
                        session?.accid = im.sessionId
                        if (im.sessionType == SessionTypeEnum.P2P) {
                            session?.name = "花漾客服"
                            session?.sessionType = 1
                        } else {
                            session?.name = im.fromNick
                            session?.sessionType = 2
                        }
                        sessionMap[im.sessionId] = session
                    }
                }
                updateShowData()
            }
        })
    }
    fun updateShowData(){
        sessionList.clear()
        sessionMap.forEach{
            sessionList.add(it.value)
        }
        sessionAdapter?.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        sessionDao.getAllDataLiveData().observe(requireActivity(), {
            if (it.isNotEmpty()){
                it.forEach{
                    sessionMap[it.accid] = it
                }
                updateShowData()
            }
        })
    }
}