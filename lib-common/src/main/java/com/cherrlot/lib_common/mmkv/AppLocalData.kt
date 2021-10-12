package com.cherrlot.lib_common.mmkv

object AppLocalData {
    //全局token
    var isFirstStart by MMKVUtil(true)

    //全局token
    var token by MMKVUtil("")
}