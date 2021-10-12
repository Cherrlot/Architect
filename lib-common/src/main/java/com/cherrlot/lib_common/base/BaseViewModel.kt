package com.cherrlot.lib_common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cherrlot.lib_common.R
import com.cherrlot.lib_common.model.UiCloseModel
import com.cherrlot.lib_common.net.constant.State
import com.cherrlot.lib_common.tool.string

/**
 * ViewModel 基类
 *
 * 提供了  **UI 关闭** [uiCloseData] 数据对象
 */
abstract class BaseViewModel
    : ViewModel()  {
    /**
     * 网络请求状态
     */
    val netRequestState by lazy {
        MutableLiveData<State>()
    }
    /**
     * 等待框提示文字
     */
    var mMsg = MutableLiveData(R.string.default_loading.string)
    /**
     * 等待弹出是否显示
     */
    val isLoading by lazy {
        MutableLiveData<Boolean>()
    }

    /** 控制 UI 组件关闭 */
    val uiCloseData = MutableLiveData<UiCloseModel>()

    /** 界面跳转控制 */
    val uiNavigationData = MutableLiveData<String>()

    /**
     * 显示等待框
     */
    fun showLoading(msg: String? = null) {
        msg?.let {
            mMsg.postValue(msg)
        }
        isLoading.postValue(true)
    }

    /**
     * 隐藏等待框
     */
    fun hideLoading() {
        isLoading.postValue(false)
    }
}