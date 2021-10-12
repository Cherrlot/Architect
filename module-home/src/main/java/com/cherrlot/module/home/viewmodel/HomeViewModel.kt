package com.cherrlot.module.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cherrlot.lib_common.base.BaseViewModel
import com.cherrlot.lib_common.bean.ArticleBean
import com.cherrlot.lib_common.bean.ArticleListBean
import com.cherrlot.lib_common.bean.BannerBean
import com.cherrlot.lib_common.ext.safe
import com.cherrlot.lib_common.ext.toast
import com.cherrlot.lib_common.log.AppLog
import com.cherrlot.lib_common.net.constant.State
import com.cherrlot.lib_common.net.constant.StateType
import com.cherrlot.lib_common.net.initiateRequest
import com.cherrlot.lib_common.repository.UserRepository
import org.koin.java.KoinJavaComponent

class HomeViewModel : BaseViewModel() {
    private val mRepository: UserRepository by KoinJavaComponent.inject(UserRepository::class.java)

    var mBannerData: MutableLiveData<List<BannerBean>> = MutableLiveData()
    var mArticlesData = MutableLiveData<ArticleListBean>()

    var mList: ArrayList<ArticleBean> = ArrayList()
    var mPage = 0

    fun getArticleList() {
        initiateRequest({ mRepository.getArticleList(mPage) },
            success = {
                mArticlesData.value = it
                netRequestState.value = State(StateType.SUCCESS)
            },
            failed = { s: String?, stateType: StateType ->
                // 加载完成
                mArticlesData.value = ArticleListBean()
                if (mPage == 0) {
                    // 第一次请求数据失败时显示错误提示
                    netRequestState.value = State(stateType, s.safe())
                } else {
                    mPage--
                    s.toast()
                }
                AppLog.e(s.safe())
            })
    }

    fun getBannerList() {
        initiateRequest({ mRepository.getBannerList() },
            success = {
                mBannerData.value = it
            },
            failed = { s: String?, _: StateType ->
                s?.let {
                    s.toast()
                    AppLog.e(s)
                }
            })
    }
}