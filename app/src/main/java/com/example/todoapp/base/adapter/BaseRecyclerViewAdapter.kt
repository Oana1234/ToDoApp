package com.example.todoapp.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

abstract class BaseRecyclerViewAdapter <T> (private val compareFun: (T, T) -> Boolean = { o, n -> o == n })
    : RecyclerView.Adapter<BaseViewHolder<T>>(), AutoUpdatableAdapter{

    private var items: MutableList<T> by Delegates.observable(mutableListOf()) { _, old, new ->
        autoNotify(old, new) { o, n -> compareFun(o, n) }
    }

    var onItemClickListener: ((T, View, Int) -> Unit) = { item, viewParent, position -> }
    var onItemLongClickListener: (T, View) -> Boolean = { item, viewParent -> true }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClickListener(item, holder.itemView, position) }
        holder.itemView.setOnLongClickListener { onItemLongClickListener(item, holder.itemView) }
    }

    protected fun getViewHolderView(parent: ViewGroup, @LayoutRes itemLayoutId: Int): View =
        LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)

    fun refreshList(list: MutableList<T>) {
        items = list
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>

    override fun getItemCount() = items.size

    private fun isEmpty(): Boolean = items.isEmpty()

    private inline fun <reified T : Any> MutableList<*>.checkItemsAre() =
        all { it is T }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

}