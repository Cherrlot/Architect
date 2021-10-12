package com.cherrlot.lib_login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cherrlot.lib_common.base.BaseAppActivity
import com.cherrlot.lib_common.ext.toast
import com.cherrlot.lib_common.log.AppLog
import com.cherrlot.lib_common.mmkv.AppLocalData
import com.cherrlot.lib_common.router.ROUTER_PATH_LOGIN
import com.cherrlot.lib_common.router.ROUTER_PATH_MAIN
import com.cherrlot.lib_login.databinding.ActivityLoginBinding
import com.cherrlot.lib_login.listener.LoginClickListener

/**
 * 登录界面
 */
@Route(path = ROUTER_PATH_LOGIN)
class LoginActivity : BaseAppActivity<LoginViewModel, ActivityLoginBinding>(), LoginClickListener {
    override val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // 设置点击事件
        mBinding.listener = this
    }

    override fun initObserve() {
        viewModel.mIsLogin.observe(this) {
            if (it) {
                "登录成功${AppLocalData.token}".toast()
                ARouter.getInstance().build(ROUTER_PATH_MAIN).navigation()
                finish()
            }
        }
        viewModel.mRegister.observe(this) {
            (mBinding.root as MotionLayout).run {
                if (it) {
                    transitionToStart()
                } else {
                    transitionToEnd()
                }
            }
        }
    }

    override fun onLoginClick() {
        viewModel.login()
    }

    override fun onRegisterClick(isRegister: Boolean) {
        viewModel.mRegister.value = isRegister
    }
}