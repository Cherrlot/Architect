package com.cherrlot.lib_common.permission

sealed class PermissionResult {
    object Grant : PermissionResult()
    class Deny(val permissions: Array<String>) : PermissionResult()
    class Rationale(val permissions: Array<String>) : PermissionResult()
}