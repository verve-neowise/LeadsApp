package com.neowise.leads.ui.custom.statusbar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.neowise.leads.R
import com.neowise.leads.domain.models.LeadStatus
import com.neowise.leads.ui.custom.selection.SelectionAdapter

class StatusAdapter(
    private var _items: List<LeadStatus>
) : SelectionAdapter<LeadStatus>() {

    override val items: List<LeadStatus>
        get() = _items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val indicator = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status_indicator, parent, false) as StatusIndicator

        return StatusHolder(indicator)
    }

    fun setItems(items: List<LeadStatus>) {
        this._items = items
        notifyItemRangeChanged(0, items.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val statusHolder = (holder as StatusHolder)
        statusHolder.bind(_items[position])
        statusHolder.itemView.setOnClickListener {
            onSelectionChangeListener?.invoke(items[position])
        }
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    private class StatusHolder(private val view: StatusIndicator) : ViewHolder(view) {

        fun bind(status: LeadStatus) {
            view.setColor(status.color)
            view.setText(status.title)
        }
    }
}

