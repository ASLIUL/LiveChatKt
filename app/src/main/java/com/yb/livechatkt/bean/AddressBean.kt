package com.yb.livechatkt.bean

data class AddressBean(var name: String?, val id: Int, var cityList: List<CityListBeanX>){


    /**
     * name : 北京
     * id : 1
     * cityList : [{"name":"北京市","id":2,"cityList":[{"name":"东城区","id":3},{"name":"西城区","id":4},{"name":"朝阳区","id":5},{"name":"丰台区","id":6},{"name":"石景山区","id":7},{"name":"海淀区","id":8},{"name":"门头沟区","id":9},{"name":"房山区","id":10},{"name":"通州区","id":11},{"name":"顺义区","id":12},{"name":"昌平区","id":13},{"name":"大兴区","id":14},{"name":"怀柔区","id":15},{"name":"平谷区","id":16},{"name":"密云县","id":17},{"name":"延庆县","id":18}]}]
     */


    class CityListBeanX(var name: String?,var id: Int,var cityList: List<CityListBean>) {
        /**
         * name : 北京市
         * id : 2
         * cityList : [{"name":"东城区","id":3},{"name":"西城区","id":4},{"name":"朝阳区","id":5},{"name":"丰台区","id":6},{"name":"石景山区","id":7},{"name":"海淀区","id":8},{"name":"门头沟区","id":9},{"name":"房山区","id":10},{"name":"通州区","id":11},{"name":"顺义区","id":12},{"name":"昌平区","id":13},{"name":"大兴区","id":14},{"name":"怀柔区","id":15},{"name":"平谷区","id":16},{"name":"密云县","id":17},{"name":"延庆县","id":18}]
         */

        class CityListBean(var name: String?,var id: Int) {
            /**
             * name : 东城区
             * id : 3
             */

        }
    }
}