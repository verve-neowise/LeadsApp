package com.neowise.leads.ui.shared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.neowise.leads.R
import com.neowise.leads.databinding.ItemCountryBinding
import com.neowise.leads.databinding.ItemHeaderBinding
import com.neowise.leads.domain.models.Country
import com.neowise.leads.ui.custom.selection.SelectionAdapter

class CountrySelectionAdapter(
    initial: List<Country> = listOf(),
) : SelectionAdapter<Country>() {

    private var countryItems: List<Item> = mapItems(initial)
    private var countries: List<Country> = initial

    override val items: List<Country>
        get() = countries

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ItemType.Data) {
            CountryHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
            )
        } else {
            HeaderHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
            )
        }
    }

    fun setItems(items: List<Country>) {
        this.countries = items
        this.countryItems = mapItems(items.sortedBy { it.title })
        notifyItemRangeChanged(0, countryItems.size)
    }

    override fun filter(criteria: String) {
        if (criteria.isEmpty()) {
            setItems(countries)
        } else {
            val filtered = countries.filter {
                matchCriteria(it, criteria)
            }
            this.countryItems = mapItems(filtered.sortedBy { it.title })
            notifyItemRangeRemoved(0, countries.size)
            notifyItemRangeInserted(0, filtered.size)
        }
    }

    private fun matchCriteria(
        it: Country,
        criteria: String
    ) =
        it.title.contains(criteria, true) ||
                it.phoneCode.contains(criteria, true)

    override fun getItemViewType(position: Int): Int {
        return countryItems[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = countryItems[position]) {
            is Item.CountryItem -> {
                val countryHolder = holder as CountryHolder

                countryHolder.bind(item.model)
                holder.itemView.setOnClickListener {
                    onSelectionChangeListener?.invoke(item.model)
                }
            }
            is Item.HeaderItem -> {
                (holder as HeaderHolder).bind(item.text)
            }
        }
    }

    override fun getItemCount(): Int {
        return countryItems.size
    }

    private object ItemType {
        const val Data = 0
        const val Header = 1
    }

    private sealed class Item(val type: Int) {
        class CountryItem(val model: Country) : Item(ItemType.Data)
        class HeaderItem(val text: String) : Item(ItemType.Header)
    }

    private class CountryHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ItemCountryBinding by viewBinding()

        fun bind(model: Country) {
            binding.title.text = itemView.context.getString(
                R.string.country_name_pattern,
                model.emoji,
                model.title
            )
            binding.code.text = model.phoneCode
        }
    }

    private class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ItemHeaderBinding by viewBinding()

        fun bind(char: String) {
            binding.root.text = char
        }
    }

    private fun mapItems(countries: List<Country>): List<Item> {
        var lastChar: Char? = null
        return buildList {
            countries.forEach {
                val char = it.title.first()
                if (char != lastChar) {
                    add(Item.HeaderItem(char.toString()))
                    lastChar = char
                }

                add(Item.CountryItem(it))
            }
        }
    }
}