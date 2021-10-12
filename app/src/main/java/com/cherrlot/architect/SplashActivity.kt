package com.cherrlot.architect

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.alibaba.android.arouter.launcher.ARouter
import com.cherrlot.architect.databinding.ActivitySplashBinding
import com.cherrlot.lib_common.base.BaseAppActivity
import com.cherrlot.lib_common.ext.safe
import com.cherrlot.lib_common.mmkv.AppLocalData
import com.cherrlot.lib_common.permission.PermissionResult
import com.cherrlot.lib_common.permission.Permissions
import com.cherrlot.lib_common.router.ROUTER_PATH_LOGIN
import com.cherrlot.lib_common.router.ROUTER_PATH_MAIN
import com.cherrlot.lib_common.util.SysUtils
import com.cherrlot.lib_common.viewmodel.SplashViewModel
import pub.devrel.easypermissions.AppSettingsDialog

class SplashActivity : BaseAppActivity<SplashViewModel, ActivitySplashBinding>() {
    override val viewModel: SplashViewModel by viewModels()

    private val mPermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        if (SysUtils.isDeviceRooted())
            MaterialDialog(this).show {
                title(com.cherrlot.lib_common.R.string.title)
                message(com.cherrlot.lib_common.R.string.root_error)
                lifecycleOwner(this@SplashActivity)
                cornerRadius(8.0f)
                cancelable(false)
                cancelOnTouchOutside(false)
                positiveButton(com.cherrlot.lib_common.R.string.confirm) { finish() }
            }
        else {
            initPermission()
        }
    }

    override fun initObserve() {
        viewModel.run {
            isUpdateOk.observe(this@SplashActivity) {
                if (it && isInitOk.value.safe()) {
                    splashFinish()
                }
            }
            isInitOk.observe(this@SplashActivity) {
                if (it && isUpdateOk.value.safe()) {
                    splashFinish()
                }
            }
        }
    }

    private fun initPermission() {
        Permissions(this).request(*mPermissions).observe(
            this, Observer {
                when (it) {
                    is PermissionResult.Grant -> {
                        viewModel.initLogSave(this)
                        viewModel.checkUpdate()
                    }
                    // 进入设置界面申请权限
                    is PermissionResult.Rationale -> {
                        AppSettingsDialog.Builder(this)
                            .setTitle("申请权限")
                            .setRationale("没有相关权限应用将无法正常运行，点击确定进入权限设置界面来进行更改")
                            .build()
                            .show()
                        finish()
                    }
                    // 进入设置界面申请权限
                    is PermissionResult.Deny -> {
                        AppSettingsDialog.Builder(this)
                            .setTitle("申请权限")
                            .setRationale("没有相关权限应用将无法正常运行，点击确定进入权限设置界面来进行更改")
                            .build()
                            .show()
                        finish()
                    }
                }
            }
        )
    }

    /**
     * 加载完成
     */
    private fun splashFinish() {
        // 是否是首次启动
        if (AppLocalData.isFirstStart) {
            // 去引导页
            ARouter.getInstance().build(ROUTER_PATH_LOGIN).navigation()
        } else if (AppLocalData.token.isBlank()){
            // 去登陆页
            ARouter.getInstance().build(ROUTER_PATH_LOGIN).navigation()
        } else {
            // 首页
            ARouter.getInstance().build(ROUTER_PATH_MAIN).navigation()
        }
        AppLocalData.isFirstStart = false

        overridePendingTransition(com.cherrlot.lib_common.R.anim.alpha_in,
            com.cherrlot.lib_common.R.anim.alpha_out)
        finish()
    }

}