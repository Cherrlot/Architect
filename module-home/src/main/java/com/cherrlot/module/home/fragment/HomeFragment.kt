package com.cherrlot.module.home.fragment

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cherrlot.lib_common.base.BaseAppFragment
import com.cherrlot.lib_common.bean.BannerBean
import com.cherrlot.lib_common.constant.ACTION_PARCELABLE
import com.cherrlot.lib_common.ext.safe
import com.cherrlot.lib_common.router.ROUTER_PATH_HOME_FRAGMENT
import com.cherrlot.lib_common.router.ROUTER_PATH_WEB
import com.cherrlot.lib_common.ui.activity.WebViewActivity
import com.cherrlot.module.home.R
import com.cherrlot.module.home.adapter.ArticleListAdapter
import com.cherrlot.module.home.databinding.FragmentHomeBinding
import com.cherrlot.module.home.viewmodel.HomeViewModel
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

@Route(path = ROUTER_PATH_HOME_FRAGMENT)
class HomeFragment : BaseAppFragment<HomeViewModel, FragmentHomeBinding>() {
    override val viewModel: HomeViewModel by activityViewModels()

    private var mBannerAdapter: BannerImageAdapter<BannerBean>? = null
    private var mAdapter: ArticleListAdapter? = null

    override val layoutResId: Int
        get() = R.layout.fragment_home

    override fun initView() {
        initBanner()
        initList()
        mBinding.refreshLayout.autoRefresh()
    }

    private fun initData() {
        viewModel.mPage = 0
        viewModel.getBannerList()
        viewModel.getArticleList()
    }

    override fun initObserve() {
        viewModel.mBannerData.observe(this) {
            mBinding.mBanner.setDatas(it)
        }
        viewModel.mArticlesData.observe(this) {
            mBinding.refreshLayout.finishRefresh()
            mBinding.refreshLayout.finishLoadMore()
            if (viewModel.mPage == 0) viewModel.mList.clear()
            it.datas?.let { list ->
                viewModel.mList.addAll(list)
                mAdapter?.notifyItemRangeChanged(viewModel.mList.size - list.size, viewModel.mList.size - 1)
            }
        }
    }

    private fun initList() {
        mAdapter = ArticleListAdapter(viewModel.mList)
        mAdapter?.itemClick {
            ARouter.getInstance().build(ROUTER_PATH_WEB)
                .withParcelable(
                    ACTION_PARCELABLE,
                    WebViewActivity.ActionModel(
                        viewModel.mList[it].id.toString(),
                        viewModel.mList[it].title.safe(),
                        viewModel.mList[it].link.safe()
                    )
                ).navigation()
        }
        mBinding.mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mBinding.mRecyclerView.adapter = mAdapter

        mBinding.refreshLayout.setOnRefreshListener {//下拉刷新
            initData()
        }
        mBinding.refreshLayout.setOnLoadMoreListener {//上拉加载
            viewModel.mPage++
            viewModel.getArticleList()
        }
        addInfoView(mBinding.llRefresh){
            viewModel.getArticleList()
        }
    }

    private fun initBanner() {
        mBannerAdapter = object : BannerImageAdapter<BannerBean>(viewModel.mBannerData.value) {
            override fun onBindView(
                holder: BannerImageHolder,
                data: BannerBean,
                position: Int,
                size: Int
            ) {
                //图片加载自己实现
                Glide.with(holder.itemView)
                    .load(data.imagePath)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView)
            }
        }
        mBinding.mBanner.setStartPosition(1)
            .setAdapter(mBannerAdapter)
            .addBannerLifecycleObserver(this) //添加生命周期观察者
            .setIndicator(CircleIndicator(requireContext()))
            .setOnBannerListener { _, position ->
                ARouter.getInstance().build(ROUTER_PATH_WEB)
                    .withParcelable(
                        ACTION_PARCELABLE,
                        WebViewActivity.ActionModel(
                            viewModel.mBannerData.value?.get(position)?.id.toString(),
                            viewModel.mBannerData.value?.get(position)?.title.safe(),
                            viewModel.mBannerData.value?.get(position)?.url.safe()
                        )
                    ).navigation()
            }
    }
}