package com.cherrlot.lib_common.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.cherrlot.lib_common.ext.hideSoftKeyboard
import com.cherrlot.lib_common.log.AppLog
import com.cherrlot.lib_common.tool.shouldHideInput

abstract class BaseActivity : AppCompatActivity(){
    /** 当前界面 Context 对象*/
    protected lateinit var mContext: AppCompatActivity
    /** 标记 - 触摸输入框以外范围是否隐藏软键盘*/
    private var touchToHideInput = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 保存当前 Context 对象
        mContext = this
    }

    override fun onPause() {
        super.onPause()
        // 移除当前获取焦点控件的焦点，防止下个界面软键盘顶起布局
        currentFocus?.clearFocus()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (touchToHideInput) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                if (shouldHideInput(currentFocus, ev)) {
                    // 需要隐藏软键盘
                    currentFocus?.hideSoftKeyboard()
                }
                return super.dispatchTouchEvent(ev)
            }
            if (window.superDispatchTouchEvent(ev)) {
                return true
            }
            return onTouchEvent(ev)
        } else {
            return super.dispatchTouchEvent(ev)
        }
    }
}