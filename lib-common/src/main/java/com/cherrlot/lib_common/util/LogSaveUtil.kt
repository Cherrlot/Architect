package com.cherrlot.lib_common.util

import com.cherrlot.lib_common.ext.safe
import com.cherrlot.lib_common.log.AppLog
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object LogSaveUtil {
    private val logFileDate = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    var TAG: String? = null

    /**
     * 开始收集日志信息
     */
    fun createLogCollector(path: String?) {
        if (path == null) {
            AppLog.d("未设置path")
            return
        }
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val cmdCollect =
                        path + File.separator + logFileDate.format(Calendar.getInstance().time) + ".txt"
                    try {
                        val commandList: MutableList<String> = ArrayList()
                        commandList.add("logcat")
                        commandList.add("-f")
                        commandList.add(cmdCollect)
                        commandList.add("-v")
                        commandList.add("time")
                        commandList.add("$TAG:I")
                        commandList.add("System.err:W") // 过滤所有的错误信息
                        commandList.add("System.out:I") // 过滤所有的错误信息
                        commandList.add("AndroidRuntime:E") //运行报错

                        // 过滤指定TAG的信息
                        commandList.add("$TAG:V")
                        commandList.add("$TAG:D")
                        commandList.add("*:S")
                        try {
                            val process = Runtime.getRuntime().exec(commandList.toTypedArray())
                            sleep(1000)
                        } catch (e: Exception) {
                            AppLog.e("CollectorThread == >" + e.message, e)
                        }
                    } catch (ex: Exception) {
                        try {
                            sleep(100)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                } catch (ex: Exception) {
                    AppLog.d(ex.message.safe())
                }
            }
        }.start()
    }
}