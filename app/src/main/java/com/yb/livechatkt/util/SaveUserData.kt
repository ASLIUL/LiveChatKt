package com.yb.livechatkt.util

import android.content.Context
import android.content.SharedPreferences
import java.lang.StringBuilder

class SaveUserData {



    companion object{
        lateinit var edit:SharedPreferences.Editor
        lateinit var sharedPreferences: SharedPreferences

        private var instance: SaveUserData? = null
            get() {
                if (field == null) {
                    field = SaveUserData()
                }
                return field
            }
        fun get(context: Context): SaveUserData{
            sharedPreferences = context.getSharedPreferences(NetConstant.spSaveKey,0);
            edit = context.getSharedPreferences(NetConstant.spSaveKey,0).edit()
            return instance!!
        }



    }


    var accId:String
        set(value) {
            edit?.apply {
                putString("accid",value)
                apply()
            }
        }
        get() =  sharedPreferences?.getString("accid","")!!

    var token:String
        set(value) {
            edit?.apply {
                putString("token",value)
                apply()
            }
        }
        get() =  sharedPreferences?.getString("token","")!!

    var imToken:String
        set(value) {
            edit?.apply {
                putString("im_token",value)
                apply()
            }
        }
        get() =  sharedPreferences?.getString("im_token","")!!

    var username:String
        set(value) {
            edit?.apply {
                putString("username",value)
                apply()
            }
        }
        get() =  sharedPreferences?.getString("username","")!!

    var id:Int
        set(value) {
            edit?.apply {
                putInt("id",value)
                apply()
            }
        }
        get() =  sharedPreferences?.getInt("id",0)!!

    var role:Int
        set(value) {
            edit?.apply {
                putInt("role",value)
                apply()
            }
        }
        get() =  sharedPreferences?.getInt("role",0)!!

    var qrCodeUrl:String
        set(value) {
            edit?.apply {
                putString("qr_code_url",value)
                apply()
            }
        }
        get() =  sharedPreferences?.getString("qr_code_url","")!!


    var notification:Boolean
        set(value) {
            edit?.apply {
                putBoolean("is_notification",value)
                apply()
            }
        }
    get() = sharedPreferences?.getBoolean("is_notification",true)




    var uuid:String
        set(value) {
            edit?.apply {
                putString("uuid",value)
                apply()
            }
        }
    get() = sharedPreferences?.getString("uuid","1111113131231")!!

    fun clearData(){
        edit?.apply{
            clear()
            apply()
        }
    }




}