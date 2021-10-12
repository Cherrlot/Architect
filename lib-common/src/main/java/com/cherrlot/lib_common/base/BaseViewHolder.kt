package com.cherrlot.lib_common.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(var mBinding: ViewDataBinding, itemView: View)  : RecyclerView.ViewHolder(itemView)