package com.yb.livechatkt.ui.activity

import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.yb.livechatkt.R
import com.yb.livechatkt.databinding.ActivityCompleteInformationLayoutBinding
import com.yb.livechatkt.databinding.DialogUpdateUserDataLayoutBinding
import com.yb.livechatkt.ui.model.PictureSelectEngine
import com.yb.livechatkt.util.*
import com.yb.livechatkt.vm.CompleteInformationViewModel
import kotlinx.android.synthetic.main.dialog_update_user_sex_layout.*

class CompleteInformationActivity : BaseAppActivity() ,View.OnClickListener {

    val viewModel by viewModels<CompleteInformationViewModel>()
    lateinit var binding:ActivityCompleteInformationLayoutBinding
    private var hashMap:MutableMap<String,Any> = HashMap()
    private var userDataDialog:AlertDialog? = null
    //1 昵称，2签名
    private var dataType:Int = 1
    private var connect:EditText? = null
    private var userDataSexDialog:AlertDialog? = null
    private var newSex:Int = 1

    override fun initView() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.lifecycleOwner = this

    }

    override fun initListener() {

        binding.addressAction.setOnClickListener {
            var alertDialog = AlertDialogUtil.selectAddressDialog(this)
            alertDialog.show()
            var layoutParams = alertDialog.window?.attributes
            layoutParams?.width = getScreenWidth()
            layoutParams?.height = (getScreenHeight() /3)*2
            layoutParams?.gravity = Gravity.BOTTOM
            layoutParams?.dimAmount = 0.6f
            layoutParams?.windowAnimations = R.style.dialog_enter_exit
            alertDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            alertDialog.window?.attributes = layoutParams
        }
        AlertDialogUtil.addressLiveData.observe(this, Observer {
            var address =
                it[NetConstant.PROVINCE_NAME].toString() + "-" + it[NetConstant.CITY_NAME].toString() + "-" + it[NetConstant.COUNTY_NAME]
            address = address.replace("\n", "").trim()
            binding.address.text = address
            hashMap[NetConstant.PROVINCE_ID] = it[NetConstant.PROVINCE_ID].toString()
            hashMap[NetConstant.CITY_ID] = it[NetConstant.CITY_ID].toString()
            hashMap[NetConstant.COUNTY_ID] = it[NetConstant.COUNTY_ID].toString()
            hashMap["address"] = address
        })

        binding.headerAction.setOnClickListener {
            PictureSelector
                .create(this)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .isGif(false)
                .cropImageWideHigh(1,1)
                .freeStyleCropEnabled(true)
                .scaleEnabled(true)
                .queryMaxFileSize(10f)
                .isReturnEmpty(false)
                .imageEngine(PictureSelectEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        Glide.with(this@CompleteInformationActivity).load(result?.get(0)!!.path).into(binding.header)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            hashMap["header"] = uriToFileQ(this@CompleteInformationActivity, Uri.parse(result?.get(0)!!.path))!!.absolutePath
                        }else{
                            hashMap["header"] = result?.get(0).path
                        }
                    }
                    override fun onCancel() {

                    }

                })


        }
        binding.nicknameAction.setOnClickListener {
            dataType = 1
            showUpdateUserDataDialog()
        }
        binding.signAction.setOnClickListener {
            dataType = 2
            showUpdateUserDataDialog()
        }
        binding.sexAction.setOnClickListener { showSexUpdateDialog() }
        binding.submit.setOnClickListener {
            Log.d(TAG, "initListener: $hashMap")
            if (hashMap.size == 8){
                viewModel.submitMyData(hashMap)
            }else{
                resources.getString(R.string.write_finish).showToast()
            }
        }
        viewModel.updateDataLiveData.observe(this, Observer {
            if (it){
                resources.getString(R.string.save_success).showToast()
            }else{
                resources.getString(R.string.save_failed).showToast()
            }
        })
    }

    private fun showUpdateUserDataDialog(){
        var builder = AlertDialog.Builder(this)
        val dialogBinding: DialogUpdateUserDataLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                this
            ), R.layout.dialog_update_user_data_layout, null, false
        )
        builder.setView(dialogBinding.root)
        userDataDialog = builder.create()
        dialogBinding.cancel.setOnClickListener {
            userDataDialog?.dismiss()
        }
        connect = dialogBinding.connect
        dialogBinding.sure.setOnClickListener(this)
        when (dataType) {
            1 -> {
                dialogBinding.actionTips.visibility = View.VISIBLE
                dialogBinding.dialogTitle.text = resources.getText(R.string.mine_nickname)
                dialogBinding.connect.hint = java.lang.String.format(
                    resources.getString(R.string.please_input_connect),
                    resources.getString(R.string.mine_nickname)
                )
                dialogBinding.tipsConnect.text = java.lang.String.format(
                    resources.getString(R.string.input_limit_hint),
                    resources.getString(R.string.mine_nickname),
                    NetConstant.CONNECT_LENGTH_NICKNAME.toString()
                )
            }
            2 -> {
                dialogBinding.actionTips.visibility = View.VISIBLE
                dialogBinding.dialogTitle.text = resources.getText(R.string.sign)
                dialogBinding.connect.hint = java.lang.String.format(
                    resources.getString(R.string.please_input_connect),
                    resources.getText(R.string.sign)
                )
                dialogBinding.tipsConnect.text = java.lang.String.format(
                    resources.getString(R.string.input_limit_hint),
                    resources.getString(R.string.sign),
                    NetConstant.CONNECT_LENGTH_SIGN.toString()
                )
            }
        }
        userDataDialog?.show()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.sure){
            if (connect?.text.toString().isEmpty()){
                resources.getString(R.string.write_something).showToast()
            }
            if (dataType == 1){
                binding.nickname.text = connect?.text.toString()
                hashMap["username"] = connect?.text.toString()

            }else if (dataType == 2){
                binding.sign.text = connect?.text.toString()
                hashMap["sign"] = connect?.text.toString()

            }
            userDataDialog?.dismiss()
        }
    }

    private fun showSexUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        val view: View = LayoutInflater.from(this)
            .inflate(R.layout.dialog_update_user_sex_layout, null, false)
        builder.setView(view)
        val sureSex = view.findViewById<TextView>(R.id.sure_sex)
        val cancelSex = view.findViewById<TextView>(R.id.cancel_sex)
        cancelSex.setOnClickListener { userDataSexDialog?.dismiss() }
        sureSex.setOnClickListener { userDataSexDialog?.dismiss() }
        val sexGroup = view.findViewById<RadioGroup>(R.id.sex_group)
        sexGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.sex_men) {
                newSex = 1
                binding.sex.text = "男"
                hashMap["sex"] = 1
            } else if (checkedId == R.id.sex_women) {
                newSex = 2
                binding.sex.text = "女"
                hashMap["sex"] = 2
            }
        }
        userDataSexDialog = builder.create()
        userDataSexDialog?.show()
    }

    override fun getLayout(): Int = R.layout.activity_complete_information_layout
}