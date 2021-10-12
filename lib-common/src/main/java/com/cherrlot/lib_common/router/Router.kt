package com.cherrlot.lib_common.router

/**
 * 路由相关
 */

/** 路由组 - APP */
const val ROUTER_GROUP_APP = "/app"

/** 路由组 - login */
const val ROUTER_GROUP_LOGIN = "/login"

/** 路由组 - common */
const val ROUTER_GROUP_COMMON = "/common"

/** 路由组 - user */
const val ROUTER_GROUP_USER = "/user"

/** 路由组 - home */
const val ROUTER_GROUP_HOME = "/home"

/** 主界面 MainActivity */
const val ROUTER_PATH_MAIN = "$ROUTER_GROUP_APP/MainActivity"

/** 主界面 HomeFragment */
const val ROUTER_PATH_HOME_FRAGMENT = "$ROUTER_GROUP_HOME/HomeFragment"

/** 网页 WebViewActivity */
const val ROUTER_PATH_WEB = "$ROUTER_GROUP_COMMON/WebViewActivity"

/** 登录 LoginActivity */
const val ROUTER_PATH_LOGIN = "$ROUTER_GROUP_LOGIN/LoginActivity"

/** 我的 MineActivity */
const val ROUTER_PATH_MINE = "$ROUTER_GROUP_USER/MineActivity"

/** 设置 SettingActivity */
const val ROUTER_PATH_SETTING = "$ROUTER_GROUP_USER/SettingActivity"

