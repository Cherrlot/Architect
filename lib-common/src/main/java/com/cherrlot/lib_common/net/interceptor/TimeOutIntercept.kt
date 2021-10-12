package com.cherrlot.lib_common.net.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * description: 单独设置超时时间
 */
class TimeOutIntercept : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url.toString()
//        return if (requestUrl.contains(ApiConstants.MIXED_PAY_URL)||requestUrl.contains(ApiConstants.ALL_DISH_LIST_URL)) {
//            //支付接口  全量菜品数据列表
//            chain.withReadTimeout(Api.PAY_TIME_OUT, TimeUnit.SECONDS).proceed(request)
//        } else {
        return chain.proceed(request)
//        }
    }
}