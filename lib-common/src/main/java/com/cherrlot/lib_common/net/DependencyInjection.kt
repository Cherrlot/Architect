package com.cherrlot.lib_common.net

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cherrlot.lib_common.BuildConfig
import com.cherrlot.lib_common.R
import com.cherrlot.lib_common.base.BaseResponse
import com.cherrlot.lib_common.base.BaseViewModel
import com.cherrlot.lib_common.net.constant.State
import com.cherrlot.lib_common.net.constant.StateType
import com.cherrlot.lib_common.net.constant.TIME_OUT
import com.cherrlot.lib_common.net.interceptor.TokenIntercept
import com.cherrlot.lib_common.net.log.InterceptorLogger
import com.cherrlot.lib_common.net.log.LoggerInterceptor
import com.cherrlot.lib_common.repository.UserRepository
import com.cherrlot.lib_common.tool.string
import com.cherrlot.lib_common.util.NetWorkUtil
import com.google.gson.JsonParser
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/** 网络请求 Module */
val netModule: Module = module {

    single {
        //缓存路径
        val logger = object : InterceptorLogger {
            override fun invoke(msg: String) {
                Logger.i(msg)
            }
        }
        OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addNetworkInterceptor(
                LoggerInterceptor(
                    logger,
                    if (BuildConfig.DEBUG) LoggerInterceptor.LEVEL_BODY else LoggerInterceptor.LEVEL_NONE
                )
            )
            .addInterceptor(TokenIntercept())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())// 通用转换器
            .addConverterFactory(GsonConverterFactory.create()) // 使用gson转换器
            .client(get())
            .build()
    }

    single<WebService> {
        get<Retrofit>().create(WebService::class.java)
    }
}

/** 数据仓库 Module */
val repositoryModule: Module = module {
    factory { UserRepository(get()) }
}

/** 适配器 Module */
val adapterModule: Module = module {
}

fun <T> BaseViewModel.initiateRequest(
    block: suspend () -> BaseResponse<T>,
    success: ((T) -> Unit)? = null,
    failed: ((String?, StateType) -> Unit)? = null
) {
    if (!NetWorkUtil.isInternetAvailable()) {
        failed?.invoke(R.string.no_net_error.string, StateType.NETWORK_ERROR)
        return
    }

    viewModelScope.launch {
        runCatching {
            block()
        }.onSuccess {
            // TODO 成功标志位需要更换
            if (it.result || it.errorCode == 0) {
                // 成功
                success?.invoke(it.data)
            } else {
                // 失败
                failed?.invoke(it.message, StateType.ERROR)
            }
        }.onFailure {
            // 异常
            NetExceptionHandle.handleException(it, failed)
        }
    }
}