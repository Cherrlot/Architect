package com.cherrlot.lib_common.model

import android.app.Activity
import android.content.Intent

/**
 * 关闭 UI 界面数据对象
 *
 * @param resultCode 返回码 默认 [Activity.RESULT_CANCELED]
 * @param result 返回数据 默认 null
 *
 */
data class UiCloseModel(
        var resultCode: Int = Activity.RESULT_CANCELED,
        var result: Intent? = null
)