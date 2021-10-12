package com.cherrlot.lib_common.router.interceptor

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import com.cherrlot.lib_common.mmkv.AppLocalData
import com.cherrlot.lib_common.router.ROUTER_PATH_LOGIN
import com.cherrlot.lib_common.router.ROUTER_PATH_MINE
import com.cherrlot.lib_common.router.ROUTER_PATH_SETTING

/**
 * 登录状态拦截器
 *
 */
@Interceptor(priority = 8, name = "登录状态拦截器")
class LoginInterceptor : IInterceptor {

    override fun init(context: Context) {
    }

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        if (needIntercept(postcard.path)) {
            // TODO 需要登录验证
            if (AppLocalData.token.isBlank()) {
                // 未登录，转发到登录页
                ARouter.getInstance().build(ROUTER_PATH_LOGIN).navigation()
                callback.onInterrupt(Exception("must login first!"))
                return
            }
        }

        // 处理完成，交还控制
        callback.onContinue(postcard)
    }

    /** 根据 [path] 判断是否需要拦截 */
    private fun needIntercept(path: String): Boolean {
        return path in arrayOf(
            ROUTER_PATH_SETTING, // 设置
            ROUTER_PATH_MINE, // 我的
        )
    }
}