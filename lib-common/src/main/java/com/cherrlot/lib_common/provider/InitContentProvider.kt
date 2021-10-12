package com.cherrlot.lib_common.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.cherrlot.lib_common.util.AppManager

/**
 * 用于初始化的 [ContentProvider]
 * > 启动自动初始化，不需要手动初始化
 *
 */
class InitContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        (context?.applicationContext as? Application)?.let { application ->
            AppManager.register(application)
        }
        return true
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }

    override fun query(p0: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {
        return null
    }
}