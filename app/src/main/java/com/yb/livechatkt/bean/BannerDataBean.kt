package com.yb.livechatkt.bean;

import com.yb.livechatkt.R;

import java.util.ArrayList;
import java.util.List;

class BannerDataBean(val imageRes:Int,val title:String,val viewType:Int) {


    companion object{
        fun getData():MutableList<BannerDataBean>{
            var list:MutableList<BannerDataBean> = ArrayList()
            list.add(BannerDataBean(R.mipmap.banner1, "相信自己,你努力的样子真的很美", 1));
            list.add(BannerDataBean(R.mipmap.banner2, "极致简约,梦幻小屋", 3));
            return list;
        }
    }

}
