package com.cherrlot.lib_common.repository

import com.cherrlot.lib_common.net.WebService
import com.cherrlot.lib_common.util.Md5Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody

/**
 * 用户Repository
 */
class UserRepository(private val webService: WebService) {
    /**
     * 登录
     */
    suspend fun login(request: RequestBody) = withContext(Dispatchers.IO) {
        webService.login(request)
    }

    suspend fun getArticleList(page: Int) = withContext(Dispatchers.IO) {
        webService.getArticleList(page)
    }

    suspend fun getBannerList() = withContext(Dispatchers.IO) {
        webService.getBannerList()
    }
}