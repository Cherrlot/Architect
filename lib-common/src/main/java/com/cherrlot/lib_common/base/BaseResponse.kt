package com.cherrlot.lib_common.base

open class BaseResponse<T>(var data: T, var result: Boolean, var message: String?, var errorCode: Int = 0)