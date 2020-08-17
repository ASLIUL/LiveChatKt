package com.yb.livechatkt.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.FragmentSessionBinding
import com.yb.livechatkt.vm.SessionViewModel

class SessionFragment : Fragment() {

    val viewmodel by viewModels<SessionViewModel>()
    lateinit var binding:FragmentSessionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session,container,false)

        return binding.root
    }
}