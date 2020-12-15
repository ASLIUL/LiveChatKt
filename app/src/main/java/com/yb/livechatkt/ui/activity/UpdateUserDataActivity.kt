package com.yb.livechatkt.ui.activity

import android.net.Uri
import android.os.Build
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
import com.yb.livechatkt.bean.Me
import com.yb.livechatkt.databinding.ActivityUpdateUserDataLayoutBinding
import com.yb.livechatkt.databinding.DialogUpdateUserDataLayoutBinding
import com.yb.livechatkt.ui.model.PictureSelectEngine
import com.yb.livechatkt.util.*
import com.yb.livechatkt.vm.UpdateUserDataViewModel
import java.io.File
import java.lang.String

class UpdateUserDataActivity : BaseAppActivity(),View.OnClickListener {

    val viewModel by viewModels<UpdateUserDataViewModel>()
    lateinit var binding:ActivityUpdateUserDataLayoutBinding
    private var newSex:Int = 1;
    //1是更新昵称 2 是性别，3是签名 4是地区 5是花漾号
    private var dataType:Int = 1
    private var connect:EditText? = null
    private var userDataDialog:AlertDialog? = null
    private var userDataSexDialog:AlertDialog? = null
    private var IMAGE_RESULT = 10000111

    override fun initView() {
        binding = DataBindingUtil.setContentView(this, getLayout())
        binding.data = viewModel
        binding.lifecycleOwner = this
        var myData = intent.getSerializableExtra("meData") as Me
        Glide.with(this).load(myData.headImg).into(binding.header)
        binding.nickname.text = myData.username
        binding.hynum.text = myData.uni_id
        binding.sex.text = if (myData.sex == 1) "男" else "女"
        newSex = myData.sex
        binding.address.text = myData.address
        binding.sign.text = myData.signature
    }

    override fun initListener() {

        viewModel.isOffLineLiveData.observe(this,{
            if (it) {offLine();finish()}
        })

        binding.liveTitleBar.leftImg.setOnClickListener { finish() }
        binding.addressAction.setOnClickListener {
            dataType = 4
            selectAddressDialog(this)
        }
        addressLiveData.observe(this, Observer {
            var address =
                it[NetConstant.PROVINCE_NAME].toString() + "-" + it[NetConstant.CITY_NAME].toString() + "-" + it[NetConstant.COUNTY_NAME]
            address = address.replace("\n", "").trim()
            binding.address.text = address
            viewModel.updateMyData(dataType, "", it)
        })
        viewModel.updateMsgLiveData.observe(this, Observer {
            if (it)
                resources.getString(R.string.save_success).showToast()
            else
                resources.getString(R.string.save_failed).showToast()
        })
        binding.signAction.setOnClickListener {
            dataType = 3
            showUpdateUserDataDialog()
        }
        binding.nicknameAction.setOnClickListener {
            dataType = 1
            showUpdateUserDataDialog()
        }
        binding.hynumAction.setOnClickListener {
            dataType = 5
            showUpdateUserDataDialog()
        }
        binding.sexAction.setOnClickListener {
            dataType = 2
            showSexUpdateDialog()
        }
        binding.headerAction.setOnClickListener {
            PictureSelector
                .create(this)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .isGif(false)
                .isEnableCrop(true)
                .enableCrop(true)
                .withAspectRatio(1,1)
                .cropImageWideHigh(400,400)
                .freeStyleCropEnabled(true)
                .scaleEnabled(true)
                .queryMaxFileSize(10f)
                .isReturnEmpty(true)
                .imageEngine(PictureSelectEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        Glide.with(this@UpdateUserDataActivity).load(result?.get(0)!!.path).into(binding.header)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            viewModel.updateMyHeader(uriToFileQ(this@UpdateUserDataActivity, Uri.parse(result?.get(0)!!.path))!!)
                        }else{
                            viewModel.updateMyHeader(File(result?.get(0).path))
                        }
                    }

                    override fun onCancel() {

                    }

                })

        }
    }

    private fun showUpdateUserDataDialog(){
        var builder = AlertDialog.Builder(this)
        val dialogBinding:DialogUpdateUserDataLayoutBinding = DataBindingUtil.inflate(
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
                dialogBinding.connect.hint = String.format(
                    resources.getString(R.string.please_input_connect),
                    resources.getString(R.string.mine_nickname)
                )
                dialogBinding.tipsConnect.text = String.format(
                    resources.getString(R.string.input_limit_hint),
                    resources.getString(R.string.mine_nickname),
                    NetConstant.CONNECT_LENGTH_NICKNAME.toString()
                )
            }
            5 -> {
                dialogBinding.actionTips.visibility = View.VISIBLE
                dialogBinding.dialogTitle.text = resources.getText(R.string.mine_hy_num)
                dialogBinding.connect.hint = String.format(
                    resources.getString(R.string.please_input_connect),
                    resources.getText(R.string.mine_hy_num)
                )
                dialogBinding.tipsConnect.text = resources.getText(R.string.hy_num_tips)
                dialogBinding.tipsConnect.append(
                    "且" + String.format(
                        resources.getString(R.string.input_limit_hint),
                        resources.getString(R.string.mine_hy_num),
                        NetConstant.CONNECT_LENGTH_HYNUM.toString()
                    )
                )
            }
            3 -> {
                dialogBinding.actionTips.visibility = View.VISIBLE
                dialogBinding.dialogTitle.text = resources.getText(R.string.sign)
                dialogBinding.connect.hint = String.format(
                    resources.getString(R.string.please_input_connect),
                    resources.getText(R.string.sign)
                )
                dialogBinding.tipsConnect.text = String.format(
                    resources.getString(R.string.input_limit_hint),
                    resources.getString(R.string.sign),
                    NetConstant.CONNECT_LENGTH_SIGN.toString()
                )
            }
        }
        userDataDialog?.show()
    }

    override fun onClick(v: View?) {
        if (v?.id== R.id.sure){
            when(dataType){
                1 -> {
                    if (connect?.text.toString().isEmpty()) {
                        String.format(
                            resources.getString(R.string.please_input_connect),
                            resources.getString(R.string.mine_nickname)
                        ).showToast()
                    }
                    binding.nickname.text = connect?.text.toString()
                    viewModel.updateMyData(dataType, connect?.text.toString(), null)
                }
                3 -> {
                    if (connect?.text.toString().isEmpty()) {
                        String.format(
                            resources.getString(R.string.please_input_connect),
                            resources.getString(R.string.mine_sign)
                        ).showToast()
                    }
                    binding.sign.text = connect?.text.toString()
                    viewModel.updateMyData(dataType, connect?.text.toString(), null)
                }
                5 -> {
                    if (connect?.text.toString().isEmpty()) {
                        String.format(
                            resources.getString(R.string.please_input_connect),
                            resources.getString(R.string.mine_hy_num)
                        ).showToast()
                    }
                    binding.hynum.text = connect?.text.toString()
                    viewModel.updateMyHyNum(connect?.text.toString())
                }
            }
            userDataDialog?.dismiss()
        }else if (v?.id==R.id.sure_sex){
            binding.sex.text = if (newSex == 1) "男" else "女"
            viewModel.updateMyData(dataType, newSex.toString(), null)
            userDataSexDialog?.dismiss()
        }
    }

    private fun showSexUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        val view: View = LayoutInflater.from(this)
            .inflate(R.layout.dialog_update_user_sex_layout, null, false)
        builder.setView(view)
        val sureSex = view.findViewById<TextView>(R.id.sure_sex)
        val cancelSex = view.findViewById<TextView>(R.id.cancel_sex)
        cancelSex.setOnClickListener(this)
        sureSex.setOnClickListener(this)
        val sexGroup = view.findViewById<RadioGroup>(R.id.sex_group)
        sexGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.sex_men) {
                newSex = 1
            } else if (checkedId == R.id.sex_women) {
                newSex = 2
            }
        }
        userDataSexDialog = builder.create()
        userDataSexDialog?.show()
    }





    override fun getLayout(): Int = R.layout.activity_update_user_data_layout
}