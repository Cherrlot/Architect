package com.cherrlot.lib_common.bind.adapter

import android.os.SystemClock
import android.view.View
import androidx.databinding.BindingAdapter

/**
 * 添加clickFilter标签，添加多次点击过滤
 */
@BindingAdapter("android:clickFilter")
fun clickFilter(view: View, clickListener: View.OnClickListener?) {
    val mHits = LongArray(2)
    view.setOnClickListener { v ->
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        mHits[mHits.size - 1] = SystemClock.uptimeMillis()
        if (mHits[0] < SystemClock.uptimeMillis() - 500) {
            clickListener?.onClick(v)
        }
    }
}