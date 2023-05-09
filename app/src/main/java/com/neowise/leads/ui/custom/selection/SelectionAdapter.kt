package com.neowise.leads.ui.custom.selection

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class SelectionAdapter<T> : RecyclerView.Adapter<ViewHolder>() {

    abstract val items: List<T>

    protected var onSelectionChangeListener: ((T) -> Unit)? = null

    fun setOnItemSelectListener(listener: (T) -> Unit) {
        onSelectionChangeListener = listener
    }

    open fun filter(criteria: String) {}

    open val selectionMode: SelectionMode
        get() = SelectionMode.SINGLE
}

enum class SelectionMode {
    SINGLE,
    MULTIPLE
}