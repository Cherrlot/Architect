package com.cherrlot.lib_common.initializer

import android.content.Context
import androidx.startup.Initializer
import com.cherrlot.lib_common.net.adapterModule
import com.cherrlot.lib_common.net.netModule
import com.cherrlot.lib_common.net.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

/**
 * Koin start up初始化
 */
class KoinInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        // 初始化 Koin
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(context)
            modules(listOf(netModule, repositoryModule, adapterModule))
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}