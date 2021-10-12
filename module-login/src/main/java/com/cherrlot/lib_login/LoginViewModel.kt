package com.cherrlot.lib_login

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.cherrlot.lib_common.base.BaseViewModel
import com.cherrlot.lib_common.constant.PASSWORD_MIN_LENGTH
import com.cherrlot.lib_common.ext.safe
import com.cherrlot.lib_common.ext.toast
import com.cherrlot.lib_common.log.AppLog
import com.cherrlot.lib_common.mmkv.AppLocalData
import com.cherrlot.lib_common.net.constant.StateType
import com.cherrlot.lib_common.net.initiateRequest
import com.cherrlot.lib_common.repository.UserRepository
import com.cherrlot.lib_common.tool.string
import com.cherrlot.lib_common.util.Md5Utils
import com.cherrlot.lib_common.util.NetWorkUtil.getRequestBody
import org.json.JSONObject
import org.koin.java.KoinJavaComponent.inject

class LoginViewModel : BaseViewModel() {
    private val mRepository: UserRepository by inject(UserRepository::class.java)

    /**
     * 登录成功
     */
    var mIsLogin = MutableLiveData<Boolean>().apply { this.value = false }

    /** 标记 - 是否是注册 */
    var mRegister: MutableLiveData<Boolean> = MutableLiveData(true)

    val mUserName: MutableLiveData<String> = MutableLiveData("")
    val mUserNameError: MutableLiveData<String> = MutableLiveData("")

    val mPassword: MutableLiveData<String> = MutableLiveData("")
    val mPasswordError: MutableLiveData<String> = MutableLiveData("")

    val mRePassword: MutableLiveData<String> = MutableLiveData("")
    val mRePasswordError: MutableLiveData<String> = MutableLiveData("")

    /** 按钮文本 */
    val mButtonStr: LiveData<String> = mRegister.map { isRegister ->
        if (isRegister) {
            // 注册
            com.cherrlot.lib_common.R.string.app_register
        } else {
            // 登录
            com.cherrlot.lib_common.R.string.app_login
        }.string
    }

    /** 确认密码是否可见 */
    val mRePasswordVisible: LiveData<Int> = mRegister.map {
        if (it) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    /**
     * 登录
     */
    fun login() {
        val name = mUserName.value
        val password = mPassword.value
        val rePassword = mRePassword.value

        if (name.isNullOrBlank()) {
            // 用户名为空
            mUserNameError.value = (com.cherrlot.lib_common.R.string.app_please_enter_user_name.string)
            return
        }
        if (password.isNullOrBlank()) {
            // 密码为空
            mPasswordError.value = (com.cherrlot.lib_common.R.string.app_password_must_not_be_empty.string)
            return
        }
        if (password.orEmpty().length < PASSWORD_MIN_LENGTH) {
            // 密码长度小于最低长度
            mPasswordError.value = (com.cherrlot.lib_common.R.string.app_password_length_must_larger_than_six.string)
            return
        }
        mPasswordError.value = ("")
        if (mRegister.value.safe(true)) {
            // 注册
            if (rePassword.isNullOrBlank()) {
                // 密码为空
                mRePasswordError.value = (com.cherrlot.lib_common.R.string.app_password_must_not_be_empty.string)
                return
            }
            if (rePassword.orEmpty().length < PASSWORD_MIN_LENGTH) {
                // 密码长度小于最低长度
                mRePasswordError.value = (com.cherrlot.lib_common.R.string.app_password_length_must_larger_than_six.string)
                return
            }
            if (rePassword != password) {
                // 密码不匹配
                mRePasswordError.value = (com.cherrlot.lib_common.R.string.app_re_set_password_not_match.string)
                return
            }
            mRePasswordError.value = ("")
        }

        showLoading(com.cherrlot.lib_common.R.string.login_loading.string)
        val params = JSONObject()
        params.put("clientId", 9)
        params.put("identifier", name)
        params.put("password", Md5Utils.Md5(password))
        initiateRequest({ mRepository.login(getRequestBody(params.toString())) },
            success = {
                // 登录成功
                mIsLogin.value = true
                AppLocalData.token = it.token.safe()
                hideLoading()
            },
            failed = { s: String?, _: StateType ->
                hideLoading()
                s?.let {
                    s.toast()
                    AppLog.e(s)
                }
            })
    }
}