package com.cherrlot.lib_common

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.cherrlot.lib_common.tool.color
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.header.MaterialHeader.SIZE_LARGE
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import kotlin.properties.Delegates

abstract class CommonApplication : MultiDexApplication() {
    companion object {
        var instance: CommonApplication by Delegates.notNull()

        //static 代码段可以防止内存泄露
        init {
            //设置全局默认配置（优先级最低，会被其他设置覆盖）
            SmartRefreshLayout.setDefaultRefreshInitializer { _: Context?, layout: RefreshLayout ->
                //开始设置全局的基本参数
                layout.setFooterHeight(40f)
                layout.setDisableContentWhenLoading(false)
                layout.setDisableContentWhenRefresh(true) //是否在刷新的时候禁止列表的操作
                layout.setDisableContentWhenLoading(true) //是否在加载的时候禁止列表的操作
                layout.setEnableOverScrollBounce(true)
            }
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context?, _: RefreshLayout? ->
                MaterialHeader(context)
                    .setSize(SIZE_LARGE)
                    .setProgressBackgroundColorSchemeColor(R.color.app_colorPrimary.color)
                    .setColorSchemeResources(R.color.app_white)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context?, _: RefreshLayout? ->
                ClassicsFooter(context)
                    .setSpinnerStyle(SpinnerStyle.Translate)
                    .setTextSizeTitle(13f)
                    .setDrawableArrowSize(15f)
                    .setDrawableProgressSize(15f)
                    .setDrawableMarginRight(10f)
                    .setFinishDuration(0)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}