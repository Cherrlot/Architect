package com.cherrlot.lib_common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAppAdapter<DB : ViewDataBinding, T>(
    private var listDatas: ArrayList<T>
) : RecyclerView.Adapter<BaseViewHolder>() {
    /** 布局 id */
    abstract val layoutResID: Int
    private var itemClick: ((Int) -> Unit)? = null
    private var itemLongClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<DB>(
            LayoutInflater.from(parent.context),
            layoutResID, parent, false
        )
        return BaseViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.let { it(position) }
        }
        holder.itemView.setOnLongClickListener {
            itemLongClick?.let { it1 -> it1(position) }
            true
        }

        convert(holder, listDatas[position], position)
    }

    abstract fun convert(holder: BaseViewHolder, data: T, position: Int)

    override fun getItemCount(): Int {
        return listDatas.size
    }

    fun itemClick(itemClick: (Int) -> Unit) {
        this.itemClick = itemClick
    }

    fun itemLongClick(itemLongClick: (Int) -> Unit) {
        this.itemLongClick = itemLongClick
    }
}