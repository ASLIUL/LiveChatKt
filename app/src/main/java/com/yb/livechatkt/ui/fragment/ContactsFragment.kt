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
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.FragmentContactsBinding
import com.yb.livechatkt.databinding.FragmentLiveBinding
import com.yb.livechatkt.vm.ContactsViewModel
import com.yb.livechatkt.vm.LiveViewModel
import kotlin.math.log

class ContactsFragment : Fragment() {

    val viewModel by viewModels<ContactsViewModel>()
    lateinit var binding: FragmentContactsBinding
    private val TAG = "ContactsFragment"

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
        viewModel.contactsLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "?????\t"+it.tid)
        })
        viewModel.getContacts()
    }

}