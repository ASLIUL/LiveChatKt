package com.yb.livechatkt.ui.fragment

import android.content.Intent
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
import com.yb.livechatkt.R
import com.yb.livechatkt.bean.Contacts
import com.yb.livechatkt.bean.HyGroup
import com.yb.livechatkt.bean.RowX
import com.yb.livechatkt.databinding.FragmentContactsBinding
import com.yb.livechatkt.databinding.FragmentLiveBinding
import com.yb.livechatkt.ui.activity.ConversationActivity
import com.yb.livechatkt.ui.adapter.ContactsListRecyclerAdapter
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast
import com.yb.livechatkt.vm.ContactsViewModel
import com.yb.livechatkt.vm.LiveViewModel
import kotlin.math.log

class ContactsFragment : BaseFragment() {

    val viewModel by viewModels<ContactsViewModel>()
    lateinit var binding: FragmentContactsBinding
    private val TAG = "ContactsFragment"
    private var contactRowXs:MutableList<RowX> = ArrayList()
    private var contactsAdapter:ContactsListRecyclerAdapter? = null
    private var contacts:Contacts? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts,container,false)
        binding.data = viewModel
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }

    private fun initView(){
        var manager = LinearLayoutManager(requireContext())
        manager.orientation = RecyclerView.VERTICAL
        binding.contactsRecycler.layoutManager = manager
        contactsAdapter = ContactsListRecyclerAdapter(requireContext(),contactRowXs)
        binding.contactsRecycler.adapter = contactsAdapter

        viewModel.contactsLiveData.observe(requireActivity(), Observer {
            contacts = it
            contactRowXs.clear()
            contactRowXs.addAll(it.page.rows)
            contactsAdapter?.notifyDataSetChanged()
        })
        viewModel.getContacts()
        binding.serviceGroup.setOnClickListener {
            if (contacts?.tid!! < 1 ){
                resources.getString(R.string.load_failed).showToast()
                return@setOnClickListener
            }
            //记得以后判断其他群,目前只有花漾客服群
            var intent = Intent(requireContext(),ConversationActivity::class.java)
            intent.putExtra(NetConstant.SESSION_TYPE,NetConstant.SESSION_TEAM)
            var hyGroup = HyGroup(contacts?.id!!,contacts?.tid!!,contacts?.tname!!)
            intent.putExtra(NetConstant.CONVERSATION_DATA,hyGroup)
            startActivity(intent)

        }

        contactsAdapter?.setItemClickListener(object:ContactsListRecyclerAdapter.OnItemClickListener{
            override fun itemClickListener(view: View, rowX: RowX, position: Int) {
                var intent = Intent(requireContext(),ConversationActivity::class.java)
                intent.putExtra(NetConstant.SESSION_TYPE,NetConstant.SESSION_P2P)
                intent.putExtra(NetConstant.CONVERSATION_DATA,rowX)
                startActivity(intent)
            }

        })
        viewModel.tokenFailed.observe(viewLifecycleOwner, Observer {
            tokenFailed(it)
        })
        viewModel.wyLoginMonitor.observe(viewLifecycleOwner, Observer {
            wyLoginFailed(it)
        })
    }

}
