package com.cherrlot.lib_common.net.interceptor

import com.cherrlot.lib_common.net.constant.AUTHORIZATION_HEADER
import okhttp3.Interceptor
import okhttp3.Response

/**
 * description:token校验
 */
class TokenIntercept : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val originRequestAuthHeader = oldRequest.header(AUTHORIZATION_HEADER)
//        if (AppLocalData.token.isBlank() || !originRequestAuthHeader.isNullOrBlank()) {
            return chain.proceed(oldRequest)
//        }
//        val newBuilder = oldRequest.newBuilder().addHeader(AUTHORIZATION_HEADER, AppLocalData.token)
//        return chain.proceed(newBuilder.method(oldRequest.method, oldRequest.body).build())
    }
}