package com.neowise.leads.ui.custom.selection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.neowise.leads.R

class SimpleSelectionAdapter<T>(
    private var _items: List<T> = listOf()
) : SelectionAdapter<T>() {

    override val items: List<T>
        get() = _items

    fun setItems(items: List<T>) {
        this._items = items
        notifyItemRangeChanged(0, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return SimpleHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_simple_selection, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textView = (holder.itemView as TextView)
        textView.text = _items[position].toString()
        textView.setOnClickListener {
            onSelectionChangeListener?.invoke(_items[position])
        }
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    private class SimpleHolder(view: View) : ViewHolder(view)
}