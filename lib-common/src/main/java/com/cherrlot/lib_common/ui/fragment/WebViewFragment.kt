package com.cherrlot.lib_common.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import com.cherrlot.lib_common.R
import com.cherrlot.lib_common.base.BaseAppFragment
import com.cherrlot.lib_common.databinding.FragmentWebViewBinding
import com.cherrlot.lib_common.viewmodel.WebViewViewModel

/**
 * WebView 页面
 */
class WebViewFragment
    : BaseAppFragment<WebViewViewModel, FragmentWebViewBinding>() {

    override val viewModel: WebViewViewModel by activityViewModels()

    override val layoutResId: Int = R.layout.fragment_web_view

    private val mUrl: String by lazy {
        viewModel.webData.value?.url.orEmpty()
    }

    /** 当前 url */
    val currentUrl: String?
        get() = mBinding.wv.url

    override fun initView() {
        // 配置 WebView
        val webSettings = mBinding.wv.settings
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        mBinding.wv.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        mBinding.wv.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    mBinding.pb.visibility = View.GONE
                } else {
                    if (View.INVISIBLE == mBinding.pb.visibility) {
                        mBinding.pb.visibility = View.VISIBLE
                    }
                    mBinding.pb.progress = newProgress
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        mBinding.wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }
        }

        // 加载页面
        mBinding.wv.loadUrl(mUrl)
    }

    override fun onResume() {
        super.onResume()
        mBinding.wv.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.wv.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.wv.clearCache(true)
        mBinding.wv.removeAllViews()
        mBinding.wv.destroy()
    }

    /** 返回 [WebView] 能否回退上一页 */
    fun canGoBack(): Boolean {
        return if (mBinding.wv.canGoBack()) {
            mBinding.wv.goBack()
            true
        } else {
            false
        }
    }

    companion object {

        /** 创建并返回 [WebViewFragment] */
        fun actionCreate(): WebViewFragment {
            return WebViewFragment()
        }
    }
}