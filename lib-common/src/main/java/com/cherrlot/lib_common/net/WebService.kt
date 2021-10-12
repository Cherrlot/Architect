package com.cherrlot.lib_common.net

import com.cherrlot.lib_common.base.BaseResponse
import com.cherrlot.lib_common.bean.ArticleListBean
import com.cherrlot.lib_common.bean.BannerBean
import com.cherrlot.lib_common.bean.UserBean
import com.cherrlot.lib_common.net.constant.LOGIN_URL
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebService {
    // 登录
    @POST(LOGIN_URL)
    suspend fun login(@Body request: RequestBody): BaseResponse<UserBean>

//    @GET("article/listproject/{page}/json")
//    suspend fun getArticleList(@Path("page") page: Int): BaseResponse<ArticleListBean>
    @GET("https://www.wanandroid.com/article/listproject/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): BaseResponse<ArticleListBean>

    @GET("https://www.wanandroid.com/banner/json")
    suspend fun getBannerList(): BaseResponse<List<BannerBean>>
}