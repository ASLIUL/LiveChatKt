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
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.HyGroup
import com.yb.livechatkt.bean.Session
import com.yb.livechatkt.databinding.FragmentSessionBinding
import com.yb.livechatkt.db.DBManager
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.ui.activity.ConversationActivity
import com.yb.livechatkt.ui.adapter.RecentContactsRecyclerAdapter
import com.yb.livechatkt.ui.adapter.SessionRecyclerAdapter
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.getConnect
import com.yb.livechatkt.vm.SessionViewModel

class SessionFragment : BaseFragment() {

    val viewModel by viewModels<SessionViewModel>()
    lateinit var binding:FragmentSessionBinding
    private val TAG = "SessionFragment"
    private val recentContactMap : MutableMap<String, RecentContact> = HashMap()
    private val recentContacts:MutableList<RecentContact> = ArrayList()
    private var recentContactsAdapter : RecentContactsRecyclerAdapter? = null

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

        viewModel.getRecentContacts()
        val manager = LinearLayoutManager(requireContext())
        manager.orientation = RecyclerView.VERTICAL
        binding.sessionRecycler.layoutManager = manager
        recentContactsAdapter = RecentContactsRecyclerAdapter(requireContext(),recentContacts)
        binding.sessionRecycler.adapter = recentContactsAdapter
        viewModel.recentContactLiveData.observe(requireActivity(), {
            it.forEach{recentContact ->
                if (recentContact.fromAccount == SaveUserData.get().accId){
                    return@forEach
                }
                recentContactMap[recentContact.contactId] = recentContact
            }
            updateShowMessage()
        })
        viewModel.historyRecentContactLiveData.observe(requireActivity(), {
            recentContactMap.clear()
            it.forEach { recentContact ->
                recentContactMap[recentContact.contactId] = recentContact
            }
            updateShowMessage()
        })
        viewModel.deleteRecentContactLiveData.observe(requireActivity(), Observer {
            recentContactMap.remove(it.contactId)
            updateShowMessage()
        })
        recentContactsAdapter?.onItemLongClickListener = object : RecentContactsRecyclerAdapter.OnItemLongClickListener{
            override fun itemLongClickListener(
                view: View,
                recentContact: RecentContact,
                position: Int
            ) {
                showMenu(view,recentContact)
            }
        }
        recentContactsAdapter?.onItemClickListener = object :RecentContactsRecyclerAdapter.OnItemClickListener{
            override fun itemClickListener(
                view: View,
                recentContact: RecentContact,
                position: Int
            ) {
                val intent = Intent(activity,ConversationActivity::class.java)
                if (recentContact.sessionType == SessionTypeEnum.Team){
                    intent.putExtra(NetConstant.SESSION_TYPE,NetConstant.SESSION_TEAM)
                    intent.putExtra(NetConstant.CONVERSATION_DATA,HyGroup(1,recentContact.contactId.toLong(),resources.getString(R.string.hy_service)))
                }else if (recentContact.sessionType == SessionTypeEnum.P2P){
                    if (SaveUserData.get().role<=2){
                        intent.putExtra(NetConstant.SESSION_TYPE,NetConstant.SESSION_HAS_SERVICE_SESSION)
                        intent.putExtra(NetConstant.CONVERSATION_DATA,recentContact)
                    }else{
                        intent.putExtra(NetConstant.SESSION_TYPE,NetConstant.SESSION_NORMAL_USER_SESSION)
                        intent.putExtra(NetConstant.CONVERSATION_DATA,recentContact)
                    }
                }
                startActivity(intent)
            }
        }
        viewModel.isOffLineLiveData.observe(requireActivity(),{
            if (it){offLine();activity?.finish()}
        })

    }
    private fun updateShowMessage(){
        recentContacts.clear()
        recentContactMap.forEach {
            recentContacts.add(it.value)
        }
        recentContactsAdapter?.notifyDataSetChanged()
    }

    private fun showMenu(view: View, recentContact: RecentContact){
        var popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.session_item_action, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            viewModel.deleteRecentContacts(recentContact)
            true
        }
        popupMenu.show()
    }

    override fun onResume() {
        super.onResume()
        //标志当前是最近会话界面
        NIMClient.getService(MsgService::class.java).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None)
    }

}