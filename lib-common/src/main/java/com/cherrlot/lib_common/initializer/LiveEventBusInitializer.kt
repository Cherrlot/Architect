package com.cherrlot.lib_common.initializer

import android.content.Context
import androidx.startup.Initializer
import com.jeremyliao.liveeventbus.LiveEventBus

class LiveEventBusInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        // 初始化LiveDataBus
        LiveEventBus
            .config()
            .lifecycleObserverAlwaysActive(true)
            .autoClear(false)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}