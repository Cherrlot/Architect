package com.cherrlot.module.home.adapter

import com.bumptech.glide.Glide
import com.cherrlot.lib_common.base.BaseAppAdapter
import com.cherrlot.lib_common.base.BaseViewHolder
import com.cherrlot.lib_common.bean.ArticleBean
import com.cherrlot.module.home.R
import com.cherrlot.module.home.databinding.ItemArticleBinding

class ArticleListAdapter(listDatas: ArrayList<ArticleBean>): BaseAppAdapter<ItemArticleBinding, ArticleBean>(listDatas) {
    override val layoutResID: Int
        get() = R.layout.item_article

    override fun convert(holder: BaseViewHolder, data: ArticleBean, position: Int) {
        (holder.mBinding as ItemArticleBinding).let { mBinding ->
            mBinding.tvTitle.text = data.title
            mBinding.tvDes.text = data.desc
            Glide.with(holder.mBinding.root.context).load(data.envelopePic).into(mBinding.ivCover)
        }
    }
}