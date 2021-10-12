package com.cherrlot.lib_common.net

import android.accounts.NetworkErrorException
import androidx.lifecycle.MutableLiveData
import com.cherrlot.lib_common.R
import com.cherrlot.lib_common.bean.ApiErrorBean
import com.cherrlot.lib_common.ext.safe
import com.cherrlot.lib_common.log.AppLog
import com.cherrlot.lib_common.net.constant.State
import com.cherrlot.lib_common.net.constant.StateType
import com.cherrlot.lib_common.tool.string
import com.google.gson.Gson
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetExceptionHandle {
    fun handleException(
        e: Throwable?,
        failed: ((String?, StateType) -> Unit)? = null
    ) {
        e?.let {
            when (it) {
                is HttpException -> {
                    var msg =""
                    when (it.code()) {
                        400, 401 -> {
                            // TODO 单独处理http异常
                            msg = try {
                                val errorBean = Gson().fromJson(
                                    it.response()?.errorBody()?.charStream(),
                                    ApiErrorBean::class.java
                                )
                                errorBean.message ?: errorBean.msg.safe()
                            } catch (e: Exception) {
                                R.string.network_wrong.string
                            }
                        }
                        403 -> msg = R.string.auth_error.string
                        404 -> msg = R.string.not_found.string
                        500 -> msg = R.string.service_error.string
                        502 -> msg = R.string.service_error.string
                        else -> msg = R.string.network_wrong.string
                    }
                    failed?.invoke("errorCode: ${it.code()}, errorMsg: $msg", StateType.NETWORK_ERROR)
                    AppLog.e(e.printStackTrace().toString())
                }
                is ConnectException -> {
                    failed?.invoke(null, StateType.NETWORK_ERROR)
                    AppLog.e(e.printStackTrace().toString())
                }
                is SocketTimeoutException -> {
                    failed?.invoke(null, StateType.NETWORK_ERROR)
                    AppLog.e(e.printStackTrace().toString())
                }
                is NetworkErrorException -> {
                    failed?.invoke(null, StateType.NETWORK_ERROR)
                    AppLog.e(e.printStackTrace().toString())
                }
                is UnknownHostException -> {
                    failed?.invoke(null, StateType.NETWORK_ERROR)
                    AppLog.e(e.printStackTrace().toString())
                }
                else -> {
                    failed?.invoke(null, StateType.NETWORK_ERROR)
                    AppLog.e(e.printStackTrace().toString())
                }
            }
        }
    }
}