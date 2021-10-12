package com.cherrlot.lib_common.util

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.cherrlot.lib_common.CommonApplication


object ToastUtil {
    fun showToast(message: String?) {
        if (!TextUtils.isEmpty(message)) Toast.makeText(CommonApplication.instance, message, Toast.LENGTH_SHORT)
            .show()
    }
}