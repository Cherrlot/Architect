package com.cherrlot.lib_common.initializer

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.tencent.mmkv.MMKV

/**
 * MMKV start up初始化
 */
class MMKVInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        MMKV.initialize((context as Application).filesDir.absolutePath + "/mmkv")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}