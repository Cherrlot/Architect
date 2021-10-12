package com.cherrlot.lib_common.base

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.cherrlot.lib_common.BR
import com.cherrlot.lib_common.R
import com.cherrlot.lib_common.constant.ACTIVITY_ANIM_DURATION
import com.cherrlot.lib_common.ext.safe
import com.cherrlot.lib_common.net.constant.StateType
import com.cherrlot.lib_common.tool.fixFontScaleResources
import com.cherrlot.lib_common.ui.dialog.ProgressDialog
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar

abstract class BaseAppActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseActivity() {
    /** 当前界面 ViewModel 对象 */
    abstract val viewModel: VM

    /** DataBinding 对象 */
    protected lateinit var mBinding: DB

    /**
     * 等待弹窗
     */
    protected var mDialog: ProgressDialog? = null

    /**
     * 网络请求信息提示
     */
    var mNetInfoView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化观察者
        initObserve()
        // 初始化状态栏工具
        initImmersionbar()
        // 添加观察者
        observeData()
    }

    override fun getResources(): Resources? {
        // 修复 app 字体大小跟随系统字体大小调节
        return fixFontScaleResources(super.getResources(), this)
    }

    /** [onCreate] 之前执行，可用于配置动画 */
    protected open fun beforeOnCreate() {
        window.run {
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
                duration = ACTIVITY_ANIM_DURATION
            }
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
                duration = ACTIVITY_ANIM_DURATION
            }
        }
    }

    /** 初始化状态栏相关配置 */
    protected open fun initImmersionBar(immersionBar: ImmersionBar) {
    }

    /** 初始化状态栏相关配置 */
    private fun initImmersionbar() {
        immersionBar {
            statusBarColor(R.color.app_colorPrimary)
            statusBarDarkFont(false)
            fitsSystemWindows(true)
            addTag(javaClass.simpleName)
        }
    }

    /**
     * 初始化观察者
     */
    protected open fun initObserve() {
    }

    override fun setContentView(layoutResID: Int) {
        // 初始化 DataBinding
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            layoutResID, null, false
        )

        // 绑定生命周期管理
        mBinding.lifecycleOwner = this

        // 绑定 ViewModel
        mBinding.setVariable(BR.viewModel, viewModel)

        // 设置布局
        super.setContentView(mBinding.root)
    }

    /** 添加观察者 */
    private fun observeData() {
        // UI 关闭
        viewModel.uiCloseData.observe(this) { close ->
            close?.let { model ->
                setResult(model.resultCode, model.result)
                finish()
            }
        }
        // 界面跳转
        viewModel.uiNavigationData.observe(this) { path ->
            ARouter.getInstance().build(path).navigation(mContext)
        }
        // 等待框
        viewModel.isLoading.observe(this) {
            if (it) {
                // 显示等待框
                if (mDialog == null) {
                    mDialog = ProgressDialog.actionShow(this, false, viewModel.mMsg.value.safe())
                } else{
                    mDialog?.show(this)
                }
            } else {
                // 隐藏等待框
                mDialog?.dismiss()
            }
        }
        // 更改提示view的状态
        viewModel.netRequestState.observe(this) {
            var imgId: Int = R.drawable.default_nonetwork
            var msg = getString(R.string.network_error)
            when (it.code) {
                StateType.NETWORK_ERROR -> {
                    // 网络错误
                    imgId = R.drawable.default_nonetwork
                    msg = getString(R.string.network_error)
                    mNetInfoView?.visibility = View.VISIBLE
                }
                StateType.EMPTY -> {
                    // 没有数据
                    imgId = R.drawable.default_nodetail
                    msg = getString(R.string.no_data_error)
                    mNetInfoView?.visibility = View.VISIBLE
                }
                StateType.ERROR -> {
                    // 其他错误
                    imgId = R.drawable.default_no_data
                    msg = it.message
                    mNetInfoView?.visibility = View.VISIBLE
                }
                StateType.SUCCESS -> {
                    // 成功
                    mNetInfoView?.visibility = View.GONE
                }
            }

            mNetInfoView?.apply {
                if (visibility == View.VISIBLE) {
                    findViewById<TextView>(R.id.msg).text = msg
                    findViewById<ImageView>(R.id.img).setImageResource(imgId)
                }
            }
        }
    }

    /**
     * 添加一个提示view
     * @param reload    点击后重新请求
     * @param parentView    需要添加提示view的父view
     */
    open fun addInfoView(parentView: ViewGroup, reload: () -> Unit) {
        if (null == mNetInfoView) {
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mNetInfoView = LayoutInflater.from(this).inflate(R.layout.layout_load_feed, null)
            mNetInfoView?.visibility = View.GONE
            parentView.addView(mNetInfoView, 0, layoutParams)
        }
        mNetInfoView?.setOnClickListener { reload() }
    }
}