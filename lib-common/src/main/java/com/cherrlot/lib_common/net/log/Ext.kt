package com.cherrlot.lib_common.net.log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cherrlot.lib_common.base.BaseResponse
import com.cherrlot.lib_common.base.BaseViewModel
import com.cherrlot.lib_common.net.NetExceptionHandle
import com.cherrlot.lib_common.net.constant.State
import com.cherrlot.lib_common.net.constant.StateType
import com.cherrlot.lib_common.util.NetWorkUtil
import kotlinx.coroutines.launch
import okhttp3.internal.platform.Platform

/** 动态参数类型 */
typealias DynamicParams = () -> String

/** 日志打印接口 */
typealias InterceptorLogger = (String) -> Unit

/** 默认日志打印接口 */
val DEFAULT_LOGGER: InterceptorLogger by lazy(LazyThreadSafetyMode.NONE) {
    object : InterceptorLogger {
        override fun invoke(msg: String) {
            Platform.get().log(msg)
        }
    }
}
