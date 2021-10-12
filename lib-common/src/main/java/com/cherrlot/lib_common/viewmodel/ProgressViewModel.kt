package com.cherrlot.lib_common.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.cherrlot.lib_common.R
import com.cherrlot.lib_common.base.BaseViewModel
import com.cherrlot.lib_common.tool.string

/**
 * 进度弹窗 ViewModel
 */
class ProgressViewModel : BaseViewModel() {

    /** 空白点击数据 */
    val blankClickData = MutableLiveData<Int>()

    /** 提示文本 */
    val hintStr: ObservableField<String> = ObservableField(R.string.default_loading.string)

    /** 空白点击事件 */
    val onBlankClick: () -> Unit = {
        blankClickData.value = 0
    }
}