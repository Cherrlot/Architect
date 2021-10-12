package com.cherrlot.lib_common.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cherrlot.lib_common.BuildConfig
import com.cherrlot.lib_common.base.BaseViewModel
import com.cherrlot.lib_common.log.AppLog
import com.cherrlot.lib_common.repository.UserRepository
import com.cherrlot.lib_common.util.DeleteFilesUtil
import com.cherrlot.lib_common.util.LogSaveUtil
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.io.File

class SplashViewModel: BaseViewModel() {
    private val mRepository: UserRepository by inject(UserRepository::class.java)
    private var mPath: String = ""
    /**
     * 检查更新
     */
    var isUpdateOk = MutableLiveData<Boolean>().apply { this.value = false }

    /**
     * 是否初始化完成
     */
    var isInitOk = MutableLiveData<Boolean>().apply { this.value = false }

    /**
     * 保存日志到文件
     */
    fun initLogSave(context: Context) {
        mPath = "${context.getExternalFilesDir(null)?.path}/log/"
        val logFolder = File(mPath)
        if (!logFolder.exists()) {
            logFolder.mkdirs()
        }
        LogSaveUtil.TAG = BuildConfig.PRINT_LOG_TAG
        LogSaveUtil.createLogCollector(logFolder.absolutePath)

        viewModelScope.launch { deleteOldFile() }
    }

    /**
     * 删除过期的文件
     */
    private fun deleteOldFile() {
        DeleteFilesUtil.deleteFiles(mPath, 5)
        isInitOk.postValue(true)
    }

    /**
     * 检查更新
     */
    fun checkUpdate() {
        isUpdateOk.value = true
    }
}