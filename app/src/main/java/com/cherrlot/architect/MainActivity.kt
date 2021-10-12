package com.cherrlot.architect

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cherrlot.architect.databinding.ActivityMainBinding
import com.cherrlot.lib_common.adapter.setFragmentAdapter
import com.cherrlot.lib_common.base.BaseAppActivity
import com.cherrlot.lib_common.base.BlankViewModel
import com.cherrlot.lib_common.ext.toast
import com.cherrlot.lib_common.router.ROUTER_PATH_HOME_FRAGMENT
import com.cherrlot.lib_common.router.ROUTER_PATH_MAIN
import com.cherrlot.lib_common.util.AppManager

@Route(path = ROUTER_PATH_MAIN)
class MainActivity: BaseAppActivity<BlankViewModel, ActivityMainBinding>() {

    override val viewModel: BlankViewModel by viewModels()

    /** 上次返回点击时间 */
    private var mExitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mFragments = mutableListOf<Fragment>()
        val fragment: Fragment = ARouter.getInstance().build(ROUTER_PATH_HOME_FRAGMENT).navigation() as Fragment
        mFragments.add(0, fragment)

        mBinding.vpMain.setFragmentAdapter(this) {
            count(mFragments.size)
            createFragment { position -> mFragments[position] }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            "再按一次退出程序".toast()
            mExitTime = System.currentTimeMillis()
        } else {
            AppManager.appExit()
            super.onBackPressed()
        }
    }
}