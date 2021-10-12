package com.cherrlot.lib_common.bean

data class ApiErrorBean(
    var status: Int,
    var message: String?,
    var msg: String?
)