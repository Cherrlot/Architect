package com.cherrlot.lib_common.net.constant

import androidx.annotation.StringRes

data class State(var code : StateType, var message : String = "", @StringRes var tip : Int = 0)