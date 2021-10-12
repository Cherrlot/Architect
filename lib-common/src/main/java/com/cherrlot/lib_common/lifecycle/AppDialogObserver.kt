package com.cherrlot.lib_common.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * dialog Lifecycle Observer
 */
internal class AppDialogObserver(private val dismiss: () -> Unit)  : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = dismiss()
}