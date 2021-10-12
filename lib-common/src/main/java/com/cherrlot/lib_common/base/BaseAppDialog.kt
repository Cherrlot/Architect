package com.cherrlot.lib_common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.cherrlot.lib_common.BR
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ktx.immersionBar

/**
 * Dialog 基类
 * > 指定 ViewModel 类型 [VM] & 指定 DataBinding 类型 [DB]
 *
 */
abstract class BaseAppDialog<VM : BaseViewModel, DB : ViewDataBinding> : BaseDialog() {
    /** 当前界面 ViewModel 对象 */
    protected abstract val viewModel: VM

    /** DataBinding 对象 */
    protected lateinit var mBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化观察者
        initObserve()

        // 添加观察者
        observeData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 取消标题栏
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 初始化 DataBinding
        mBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

        // 绑定生命周期管理
        mBinding.lifecycleOwner = this

        // 绑定 ViewModel
        mBinding.setVariable(BR.viewModel, viewModel)

        initView()

        // 初始化布局
        mRootView = mBinding.root

        initImmersionBar()

        return mRootView
    }

    /** 初始化状态栏相关 */
    private fun initImmersionBar() {
        immersionBar {
            // 同步所在 Activity 状态栏
            getTag(mContext.javaClass.simpleName)
        }
    }

    /**
     * 初始化观察者
     */
    protected open fun initObserve() {
    }

    /**
     * 添加观察者
     */
    private fun observeData() {
        // UI 关闭
        viewModel.uiCloseData.observe(this) { close ->
            close?.let {
                dismiss()
            }
        }
        // 界面跳转
        viewModel.uiNavigationData.observe(this) { path ->
            ARouter.getInstance().build(path).navigation(mContext)
        }
    }

    /** 使用 [activity] 显示弹窗 */
    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, javaClass.simpleName)
    }

    /** 使用 [fragment] 显示弹窗 */
    fun show(fragment: Fragment) {
        show(fragment.childFragmentManager, javaClass.simpleName)
    }
}