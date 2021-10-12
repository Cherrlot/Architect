package com.cherrlot.lib_common.util

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import com.cherrlot.lib_common.CommonApplication
import com.cherrlot.lib_common.R
import com.cherrlot.lib_common.ext.toast
import java.io.File


object SysUtils {
    /**
     * 日志文件目录
     */
    lateinit var LOG_PATH: String;

    /**
     * 数据文件目录
     */
    lateinit var DATA_PATH: String;

    /**
     * 图片目录
     */
    lateinit var IMG_PATH: String;

    /**
     * 下载目录
     */
    lateinit var DOWNLOAD_PATH: String;

    fun dp2Px(context: Context, dp: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun px2Dp(context: Context, px: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }


    // 获取当前APP名称
    fun getAppName(context: Context): String? {
        val packageManager = context.packageManager
        val applicationInfo: ApplicationInfo
        applicationInfo = try {
            packageManager.getApplicationInfo(context.packageName, 0)
        } catch (e: java.lang.Exception) {
            return context.resources.getString(R.string.app_name)
        }
        return packageManager.getApplicationLabel(applicationInfo).toString()
    }

    fun getAppVersion(): String? {
        val context: Context = CommonApplication.instance
        val manager: PackageManager = context.packageManager
        return try {
            val info: PackageInfo = manager.getPackageInfo(context.packageName, 0)
            info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "1.0.0"
        }
    }

    fun getAppVersionCode(): Long {
        val context: Context = CommonApplication.instance
        val manager: PackageManager = context.packageManager
        return try {
            val info: PackageInfo = manager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.longVersionCode
            } else {
                info.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            1
        }
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    fun getSystemModel(): String? {
        return try {
            Build.MODEL
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    fun getDeviceBrand(): String? {
        return try {
            Build.BRAND
        } catch (e: Exception) {
            ""
        }
    }

    fun initFiles() {
        var file = File(CommonApplication.instance.getExternalFilesDir(null), "log")
        if (!file.exists()) file.mkdirs()
        LOG_PATH = file.canonicalPath
        file = File(CommonApplication.instance.getExternalFilesDir(null), "data")
        if (!file.exists()) file.mkdirs()
        DATA_PATH = file.canonicalPath
        file = File(CommonApplication.instance.getExternalFilesDir(null), "images")
        if (!file.exists()) file.mkdirs()
        IMG_PATH = file.canonicalPath
        file = File(CommonApplication.instance.getExternalFilesDir(null), "download")
        if (!file.exists()) file.mkdirs()
        DOWNLOAD_PATH = file.canonicalPath
    }

    fun getScreenWidth(activity: Activity): Int {
        var outSize = Point()
        var width = 0
        val windowManager = activity.windowManager
        val display = windowManager.defaultDisplay
        display.getSize(outSize)
        width = outSize.x
        return width
    }

    fun getScreenHeight(activity: Activity): Int {
        var outSize = Point()
        var height = 0
        val windowManager = activity.windowManager
        val display = windowManager.defaultDisplay
        display.getSize(outSize)
        height = outSize.y
        return height
    }

    fun showDM(activity: Activity) {
        //获取屏幕分辨率
        val metric = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metric)
        val width = metric.widthPixels // 宽度（PX）
        val height = metric.heightPixels // 高度（PX）
        val density = metric.density // 密度（0.75 / 1.0 / 1.5）
        val densityDpi = metric.densityDpi // 密度DPI（120 / 160 / 240）

        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        val screenWidth = (width / density).toInt() //屏幕宽度(dp)
        val screenHeight = (height / density).toInt() //屏幕高度(dp)

        """宽度:$width 高度:$height 密度:$density 密度DPI:$densityDpi
            屏幕dp宽度：$screenWidth 屏幕dp高度：$screenHeight""".toast()
    }

    /**
     * 检测root权限
     *
     * @return
     */
    fun isDeviceRooted(): Boolean {
        val locations = arrayOf(
            "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
            "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
            "/system/sbin/", "/usr/bin/", "/vendor/bin/"
        )
        for (location in locations) {
            if (File(location + "su").exists()) {
                return true
            }
        }
        return false
    }
}