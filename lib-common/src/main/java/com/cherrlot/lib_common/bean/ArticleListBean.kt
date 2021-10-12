package com.cherrlot.lib_common.bean

data class ArticleListBean(
    var curPage: Int = 0,
    var offset: Int = 0,
    var over: Boolean = true,
    var pageCount: Int = 0,
    var size: Int = 0,
    var total: Int = 0,
    var datas: List<ArticleBean>? = null
)

data class ArticleBean(
    var apkLink: String? = null,
    var author: String? = null,
    var chapterId: Int = 0,
    var chapterName: String? = null,
    var isCollect: Boolean = false,
    var courseId: Int = 0,
    var desc: String? = null,
    var envelopePic: String? = null,
    var id: Int = 0,
    var originId: Int = -1,
    var link: String? = null,
    var niceDate: String? = null,
    var origin: String? = null,
    var projectLink: String? = null,
    var publishTime: Long = 0,
    var title: String? = null,
    var visible: Int = 0,
    var zan: Int = 0,
    var isFresh: Boolean = false,
    var isShowImage: Boolean = true,
    // 分类name
    var navigationName: String? = null
)