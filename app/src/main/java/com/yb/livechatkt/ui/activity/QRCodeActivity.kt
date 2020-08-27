package com.yb.livechatkt.ui.activity

import android.graphics.Bitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.ActivityQrCodeLayoutBinding
import com.yb.livechatkt.util.QRCodeUtil
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.savePhoto
import kotlinx.coroutines.launch

class QRCodeActivity : BaseAppActivity() {

    lateinit var binding:ActivityQrCodeLayoutBinding
    var bitmap:Bitmap? = null

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        bitmap = QRCodeUtil.createQRImage(SaveUserData.get().qrCodeUrl,300,300)
        Glide.with(this).load(bitmap).into(binding.qrCode)
    }

    override fun initListener() {
        binding.liveTitleBar.leftImg.setOnClickListener { finish() }
        binding.save.setOnClickListener {
            lifecycleScope.launch { savePhoto(this@QRCodeActivity,bitmap!!) }
        }

    }

    override fun getLayout(): Int = R.layout.activity_qr_code_layout
}