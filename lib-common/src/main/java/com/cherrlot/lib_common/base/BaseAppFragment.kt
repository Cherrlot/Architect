package com.cherrlot.lib_common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.cherrlot.lib_common.BR
import com.cherrlot.lib_common.R
import com.cherrlot.lib_common.ext.safe
import com.cherrlot.lib_common.net.constant.StateType
import com.cherrlot.lib_common.ui.dialog.ProgressDialog

abstract class BaseAppFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseFragment() {

    /** 当前界面 ViewModel 对象 */
    protected abstract val viewModel: VM

    /** DataBinding 对象 */
    protected lateinit var mBinding: DB

    /** 标记 - 第一次加载 */
    protected var firstLoad = true
        private set

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

        // 绑定观察者
        observeData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (null == rootView) {
            // 初始化 DataBinding
            mBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

            // 绑定生命周期管理
            mBinding.lifecycleOwner = this

            // 绑定 ViewModel
            mBinding.setVariable(BR.viewModel, viewModel)

            rootView = mBinding.root

            // 初始化布局
            initView()
        } else {
            (rootView?.parent as? ViewGroup?)?.removeView(rootView)
        }

        return rootView
    }

    /**
     * 初始化观察者
     */
    protected open fun initObserve() {
    }

    override fun onPause() {
        super.onPause()

        // 标记不是第一次加载
        firstLoad = false
    }

    /**
     * 添加观察者
     */
    private fun observeData() {
        // UI 关闭
        viewModel.uiCloseData.observe(this) { close ->
            close?.let { model ->
                mContext.setResult(model.resultCode, model.result)
                mContext.finish()
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
                    mDialog = ProgressDialog.actionShow(
                        requireActivity(),
                        false,
                        viewModel.mMsg.value.safe()
                    )
                } else {
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
            mNetInfoView =
                LayoutInflater.from(requireContext()).inflate(R.layout.layout_load_feed, null)
            mNetInfoView?.visibility = View.GONE
            parentView.addView(mNetInfoView, 0, layoutParams)
        }
        mNetInfoView?.setOnClickListener {
            mNetInfoView?.visibility = View.GONE
            reload()
        }
    }
}