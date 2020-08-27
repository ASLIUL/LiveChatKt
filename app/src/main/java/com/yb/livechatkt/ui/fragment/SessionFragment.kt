package com.yb.livechatkt.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.Session
import com.yb.livechatkt.databinding.FragmentSessionBinding
import com.yb.livechatkt.db.DBManager
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.ui.activity.ConversationActivity
import com.yb.livechatkt.ui.adapter.SessionRecyclerAdapter
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.getConnect
import com.yb.livechatkt.vm.SessionViewModel

class SessionFragment : BaseFragment() {

    val viewModel by viewModels<SessionViewModel>()
    lateinit var binding:FragmentSessionBinding
    val sessionDao = DBManager.getDBInstance().getSessionDao()
    private var sessionAdapter:SessionRecyclerAdapter? = null
    private var sessionList:MutableList<Session> = ArrayList()
    private var sessionMap:MutableMap<String, Session>  = HashMap()
    private val TAG = "SessionFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session, container, false)
        initView()
        return binding.root
    }
    private fun initView(){

        var manager = LinearLayoutManager(requireContext())
        manager.orientation = RecyclerView.VERTICAL
        binding.sessionRecycler.layoutManager = manager
        sessionAdapter = SessionRecyclerAdapter(requireContext(), sessionList)
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
                    val messageMap = im.remoteExtension
                    if (sessionMap.containsKey(im.sessionId)) {
                        var session = sessionMap[im.sessionId]
                        session?.lastMsg = getConnect(im)
                        session?.lastMsgTime = im.time
                        session?.unRead = 1
                        val direct = if (im.direct == MsgDirectionEnum.In) 1 else 2
                        var  name = ""
                        if (direct == 2) {
                            name = try {
                                if (messageMap.containsKey("toName")) {
                                    messageMap["toName"].toString()
                                } else {
                                    im.sessionId
                                }
                            }catch (e:java.lang.Exception){
                                im.sessionId
                            }
                        }else if (direct == 1){
                            name = im.fromNick
                        }
                        session?.name = name
                        viewModel.updateSession(session!!)
                    } else {
                        var sessionType = if (im.sessionType == SessionTypeEnum.P2P) { 1 } else { 2 }
                        val direct = if (im.direct == MsgDirectionEnum.In) 1 else 2
                        var  name = ""
                        if (direct == 2) {
                            name = try {
                                if (messageMap.containsKey("toName")) {
                                    messageMap["toName"].toString()
                                } else {
                                    im.sessionId
                                }
                            }catch (e:java.lang.Exception){
                                im.sessionId
                            }
                        }else if (direct == 1){
                            name = im.fromNick
                        }
                        var session = Session(
                            im.sessionId,
                            name,
                            getConnect(im),
                            im.time,
                            1,
                            LiveChatUrl.headerBaseUrl + im.sessionId,
                            sessionType
                        )
                        viewModel.insertSession(session)
                    }
                }
                //updateShowData()
            }
        })

        sessionAdapter?.onItemClickListener = object : SessionRecyclerAdapter.OnItemClickListener{
            override fun itemClickListener(view: View, session: Session, position: Int) {
                if (session.sessionType == 1){
                    if (SaveUserData.get().role < 3){
                        val intent = Intent(requireContext(), ConversationActivity::class.java)
                        intent.putExtra(
                            NetConstant.SESSION_TYPE,
                            NetConstant.SESSION_HAS_SERVICE_SESSION
                        )
                        intent.putExtra(NetConstant.CONVERSATION_DATA, session)
                        startActivity(intent)
                    }else{
                        val intent = Intent(requireContext(), ConversationActivity::class.java)
                        intent.putExtra(
                            NetConstant.SESSION_TYPE,
                            NetConstant.SESSION_NORMAL_USER_SESSION
                        )
                        intent.putExtra(NetConstant.CONVERSATION_DATA, session)
                        startActivity(intent)
                    }
                }
            }
        }
        /*
        sessionAdapter?.onItemLongClickListener = object : SessionRecyclerAdapter.OnItemLongClickListener{
            override fun itemLongClickListener(view: View, session: Session, position: Int) {
                showMenu(view, session)
            }
        }
        */

        viewModel.historyMessageLiveData.observe(requireActivity(), Observer {
            try {
                it.forEach {
                    val map: Map<String, Any>? = it.extension
                    Log.d(TAG, "initView: ${getConnect(it)}")
                    if (sessionMap.containsKey(it.contactId)) {
                        var session = sessionMap[it.contactId]
                        session?.lastMsgTime = it.time
                        session?.lastMsg = getConnect(it)
                        try {
                            if (map?.containsKey("name")!!) session?.name =
                                map["name"].toString() else session?.name = it.contactId
                        }catch (e:Exception){
                            session?.name = it.contactId
                        }
                        viewModel.updateSession(session!!)
                    } else {
                        var name = try {
                            if (map?.containsKey("name")!!) map["name"].toString() else it.fromNick
                        }catch (e:Exception){
                            it.fromNick
                        }

                        var sessionType = 1
                        if (it.sessionType == SessionTypeEnum.P2P) sessionType =
                            1 else sessionType = 2
                        var session = Session(
                            it.contactId,
                            name,
                            getConnect(it),
                            it.time,
                            1,
                            LiveChatUrl.headerBaseUrl + it.contactId,
                            sessionType
                        )
                        viewModel.insertSession(session)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        })
        viewModel.getHistorySession()

    }
    private fun updateShowData(){
        sessionList.clear()
        sessionMap.forEach{
            sessionList.add(it.value)
        }
        sessionAdapter?.notifyDataSetChanged()
    }

    private fun showMenu(view: View, session: Session){
        var popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.session_item_action, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            viewModel.deleteSession(session)
            true
        }
        popupMenu.show()
    }

    override fun onResume() {
        super.onResume()
        //标志当前是最近会话界面
        NIMClient.getService(MsgService::class.java).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None)
    }

    override fun onStart() {
        super.onStart()
        sessionDao.getAllDataLiveData().observe(requireActivity(), { list ->
            if (list.isNotEmpty()) {
                list.forEach {
                    sessionMap[it.accid] = it
                }
                updateShowData()
            }
        })
    }
}