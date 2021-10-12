package com.cherrlot.lib_common.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.alibaba.android.arouter.facade.annotation.Route
import com.cherrlot.lib_common.R
import com.cherrlot.lib_common.base.BaseAppActivity
import com.cherrlot.lib_common.constant.ACTION_PARCELABLE
import com.cherrlot.lib_common.databinding.ActivityWebviewBinding
import com.cherrlot.lib_common.ext.startTargetActivity
import com.cherrlot.lib_common.router.ROUTER_PATH_WEB
import com.cherrlot.lib_common.tool.jumpToBrowser
import com.cherrlot.lib_common.ui.fragment.WebViewFragment
import com.cherrlot.lib_common.viewmodel.WebViewViewModel
import kotlinx.parcelize.Parcelize

/**
 * H5 界面
 */
@Route(path = ROUTER_PATH_WEB)
class WebViewActivity
    : BaseAppActivity<WebViewViewModel, ActivityWebviewBinding>() {

    override val viewModel: WebViewViewModel by viewModels()

    /** WebView Fragment 对象 */
    private val webViewFragment: WebViewFragment by lazy {
        WebViewFragment.actionCreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        // 获取网页数据
        intent.getParcelableExtra<ActionModel>(ACTION_PARCELABLE)?.let { data ->
            viewModel.webData.value = data
        }

        // 加载 Fragment
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcv, webViewFragment)
        }
    }

    override fun onBackPressed() {
        if (!webViewFragment.canGoBack()) {
            super.onBackPressed()
        }
    }

    override fun initObserve() {
        // 浏览器打开
        viewModel.jumpBrowser.observe(this) {
            jumpToBrowser(webViewFragment.currentUrl, mContext)
        }
    }

    /**
     * 界面跳转数据 Model
     *
     * @param id 文章 id
     * @param title 标题
     * @param url 打开链接
     */
    @Parcelize
    data class ActionModel(
            val id: String,
            val title: String,
            val url: String
    ) : Parcelable

    companion object {

        /** 使用 [context] 打开 [WebViewActivity] 界面，传递参数网页数据[webData] */
        fun actionStart(context: Context, webData: ActionModel?) {
            context.startTargetActivity<WebViewActivity>(bundleOf(
                    ACTION_PARCELABLE to webData
            ))
        }
    }
}