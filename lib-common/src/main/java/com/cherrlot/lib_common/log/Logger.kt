@file:Suppress("unused")

package com.cherrlot.lib_common.log

import android.util.Log
import com.cherrlot.lib_common.BuildConfig
import com.cherrlot.lib_common.ext.getStackTraceString

/**
 * 内部使用日志打印
 */
object AppLog {

    private var mLogEnable = false

    /**
     * 设置日志开关
     *
     * @param logEnable 是否打印日志
     */
    @JvmStatic
    fun logEnable(logEnable: Boolean) {
        mLogEnable = logEnable
    }

    /**
     * 打印 ERROR 信息
     *
     * @param message 日志信息
     */
    @JvmStatic
    fun e( message: String) {
//        if (mLogEnable) {
            log(Log.ERROR, message, null)
//        }
    }

    /**
     * 打印 ERROR 信息
     *
     * @param throwable 异常对象
     * @param message 日志信息
     */
    @JvmStatic
    fun e(throwable: Throwable?, message: String) {
//        if (mLogEnable) {
            log(Log.ERROR, message, throwable)
//        }
    }

    /**
     * 打印 ERROR 信息
     *
     * @param throwable 异常对象
     * @param message 日志信息
     */
    @JvmStatic
    fun e( message: String, throwable: Throwable?) {
//        if (mLogEnable) {
            log(Log.ERROR, message, throwable)
//        }
    }

    /**
     * 打印 INFO 信息
     *
     * @param message 日志信息
     */
    @JvmStatic
    fun i(message: String) {
        if (mLogEnable) {
            log(Log.INFO, message, null)
        }
    }

    /**
     * 打印 DEBUG 信息
     *
     * @param message 日志信息
     */
    @JvmStatic
    fun d(message: String) {
        if (mLogEnable) {
            log(Log.DEBUG, message, null)
        }
    }

    /**
     * 打印 ERROR 信息
     *
     * @param tag 临时标签
     * @param message 日志信息
     */
    @JvmStatic
    fun e(tag: String?, message: String) {
//        if (mLogEnable) {
            log(Log.ERROR, tag, message, null)
//        }
    }

    /**
     * 打印 ERROR 信息
     *
     * @param tag 临时标签
     * @param throwable 异常对象
     * @param message 日志信息
     */
    @JvmStatic
    fun e(tag: String?, throwable: Throwable?, message: String) {
//        if (mLogEnable) {
            log(Log.ERROR, tag, message, throwable)
//        }
    }

    /**
     * 打印 INFO 信息
     *
     * @param tag 临时标签
     * @param message 日志信息
     */
    @JvmStatic
    fun i(tag: String?, message: String) {
        if (mLogEnable) {
            log(Log.INFO, tag, message, null)
        }
    }

    /**
     * 打印 DEBUG 信息
     *
     * @param tag 临时标签
     * @param message 日志信息
     */
    @JvmStatic
    fun d(tag: String?, message: String) {
        if (mLogEnable) {
            log(Log.DEBUG, tag, message, null)
        }
    }

    private fun log(priority: Int, message: String?, throwable: Throwable?) {
        var msg = message.orEmpty()
        if (throwable != null && message != null) {
            msg += " : " + throwable.getStackTraceString()
        }
        if (throwable != null && message == null) {
            msg = throwable.getStackTraceString()
        }
        if (message.isNullOrBlank()) {
            msg = "Empty/NULL log message"
        }
        Log.println(priority, BuildConfig.PRINT_LOG_TAG,
            "[${Thread.currentThread().name}] ${codeLine(Thread.currentThread().stackTrace[4])} $msg")
    }

    private fun log(priority: Int, tag: String?, message: String?, throwable: Throwable?) {
        var msg = message.orEmpty()
        if (throwable != null && message != null) {
            msg += " : " + throwable.getStackTraceString()
        }
        if (throwable != null && message == null) {
            msg = throwable.getStackTraceString()
        }
        if (message.isNullOrBlank()) {
            msg = "Empty/NULL log message"
        }
        val tempTag = "lib_base_tag${if (tag == null || tag.isBlank()) "" else "-$tag"}"
        Log.println(priority, tempTag,
            "[${Thread.currentThread().name}] ${codeLine(Thread.currentThread().stackTrace[4])} $msg")
    }

    private fun codeLine(ste: StackTraceElement): String {
        val buf = StringBuilder()
        buf.append("${ste.className.split(".").last()}.${ste.methodName}")
        if (ste.isNativeMethod) {
            buf.append("(Native Method)")
        } else {
            val fName = ste.fileName
            if (fName == null) {
                buf.append("(Unknown Source)")
            } else {
                val lineNum = ste.lineNumber
                buf.append('(')
                buf.append(fName)
                if (lineNum >= 0) {
                    buf.append(':').append(lineNum)
                }
                buf.append("):")
            }
        }
        return buf.toString()
    }
}
