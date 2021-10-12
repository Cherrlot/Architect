package com.cherrlot.lib_common.ext

import com.cherrlot.lib_common.util.ToastUtil

fun String?.safe(default: String = ""): String = this ?: default
fun Int?.safe(default: Int = 0) = this ?: default
fun Long?.safe(default: Long = 0) = this ?: default
fun Boolean?.safe(default: Boolean = false) = this ?: default
fun Float?.safe(default: Float = 0f) = this ?: default
fun Double?.safe(default: Double = 0.0) = this ?: default

fun String?.toast() = ToastUtil.showToast(this.safe())