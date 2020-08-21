package com.yb.livechatkt.net

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.SaveUserData
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class RetrofitUtil {

    private  val TAG = "RetrofitUtil"
    //private val MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=UTF-8")

    companion object{
        private lateinit var retrofit:Retrofit


        private var instance: RetrofitUtil? = null
            get() {
                if (field == null) {
                    field = RetrofitUtil()
                }
                return field
            }
        fun get(context: Context): RetrofitUtil {
            var builder:OkHttpClient.Builder = OkHttpClient().newBuilder()
                .addInterceptor(LiveChatInterceptor(context))
                .connectTimeout(NetConstant.API_CONNECT_TIME_OUT,TimeUnit.SECONDS)
                .readTimeout(NetConstant.API_READ_TIME_OUT,TimeUnit.SECONDS)
                .writeTimeout(NetConstant.API_WRITE_TIME_OUT,TimeUnit.SECONDS)

            retrofit = Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(LiveChatUrl.baseUrl)
                .build()
            return instance!!
        }
    }

    fun <T> createService(service: Class<T>) : T {
        return retrofit.create(service)
    }



    class LiveChatInterceptor(context: Context) : Interceptor{

        var context = context

        override fun intercept(chain: Interceptor.Chain): Response {
            var builder = chain.request().newBuilder();
            builder.addHeader("uuid",SaveUserData.get().uuid)
            builder.addHeader("token",SaveUserData.get().token)
            return chain.proceed(builder.build())
        }

    }

    /**
     * 通过参数 Map 合集
     * @param paramsMap
     * @return
     */
    fun createJsonRequest(paramsMap: HashMap<String, Any>): RequestBody {
        val gson = Gson()
        val strEntity = gson.toJson(paramsMap)
        Log.d(TAG, "createJsonRequest: $strEntity")
        return RequestBody.create("application/json;charset=UTF-8".toMediaTypeOrNull(), strEntity)
    }

    /**
     * 通过参数 Object 合集
     * @param o
     * @return
     */
    fun createJsonRequest(o: Any): RequestBody {
        val gson = Gson()
        val strEntity = gson.toJson(o)
        Log.d(TAG, "createJsonRequest: $strEntity")
        return RequestBody.create("application/json;charset=UTF-8".toMediaTypeOrNull(), strEntity)
    }





}