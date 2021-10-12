package com.cherrlot.lib_common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.cherrlot.lib_common.CommonApplication
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.regex.Pattern

/**
 * 网络工具
 *
 * @author create by Chenlong
 */
object NetWorkUtil {
    /**
     * 获取请求体
     */
    fun getRequestBody(json: String): RequestBody {
        return json.toRequestBody("application/json".toMediaTypeOrNull())
    }
    /**
     * 是否有网络
     *
     * @param context
     * @return
     */
    @Suppress("DEPRECATION")
    fun isInternetAvailable(): Boolean {
        var result = false
        val cm = CommonApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            cm?.run {
//                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
//                    result = when {
//                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                        else -> false
//                    }
//                }
//            }
//        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
//        }
        return result
    }

    /**
     * 验证ip格式
     *
     * @param addr
     * @return
     */
    fun isIP(addr: String): Boolean {
        if (addr.length < 7 || addr.length > 15 || "" == addr) {
            return false
        }
        /*
         * 判断IP格式和范围
         */
        val rexp =
            "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"
        val pat = Pattern.compile(rexp)
        val mat = pat.matcher(addr)
        val ipAddress = mat.find()
        //============对之前的ip判断的bug在进行判断
        if (ipAddress) {
            val ips = addr.split("\\.".toRegex()).toTypedArray()
            return if (ips.size == 4) {
                try {
                    for (ip in ips) {
                        if (ip.toInt() < 0 || ip.toInt() > 255) {
                            return false
                        }
                    }
                } catch (e: Exception) {
                    return false
                }
                true
            } else {
                false
            }
        }
        return ipAddress
    }


    /**
     * 验证网址是否合法
     *
     * @param str
     * @return
     */
    fun isURL(str: String): Boolean {
        val regex = ("^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                //                 + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$")
        return match(regex, str)
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private fun match(regex: String, str: String): Boolean {
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(str)
        return matcher.matches()
    }
}