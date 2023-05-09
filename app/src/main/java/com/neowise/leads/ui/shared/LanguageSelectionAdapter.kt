package com.neowise.leads.ui.shared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.neowise.leads.R
import com.neowise.leads.databinding.ItemLanguageBinding
import com.neowise.leads.domain.models.Language
import com.neowise.leads.ui.custom.selection.SelectionAdapter
import com.neowise.leads.ui.custom.selection.SelectionMode

class LanguageSelectionAdapter(
    private var initial: List<Language> = listOf()
) : SelectionAdapter<Language>() {

    private var _selectedItems = mutableListOf<Language>()

    private var languages: List<Language> = initial

    var selectedItems: List<Language>
        get() = _selectedItems
        set(value) {

            _selectedItems = languages.filter { lang ->
                value.indexOfFirst { it.id == lang.id } != -1
            }.toMutableList()

            if (_selectedItems.isNotEmpty()) {
                onSelectionChangeListener?.invoke(_selectedItems.first())
                notifyItemRangeChanged(0, languages.size)
            }
        }

    override val items: List<Language>
        get() = languages

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LanguageHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false)
        )
    }

    override val selectionMode: SelectionMode
        get() = SelectionMode.MULTIPLE

    fun setItems(items: List<Language>) {
        this.initial = items
        this.languages = items.sortedBy { it.title.first() }
        notifyItemRangeChanged(0, items.size)
    }

    override fun filter(criteria: String) {
        if (criteria.isEmpty()) {
            setItems(initial)
        }
        else {
            val filtered = initial.filter {
                matchCriteria(it, criteria)
            }
            this.languages = filtered
            notifyItemRangeRemoved(0, initial.size)
            notifyItemRangeInserted(0, filtered.size)
        }
    }

    private fun matchCriteria(it: Language, criteria: String): Boolean {
        return it.title.contains(criteria, true)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = languages[position]

        val isSelected  = _selectedItems.contains(item)
        val languageHolder = holder as LanguageHolder

        languageHolder.bind(item, isSelected)

        languageHolder.itemView.setOnClickListener {

            languageHolder.isSelected = !languageHolder.isSelected

            if (languageHolder.isSelected) {
                _selectedItems.add(item)
            } else {
                _selectedItems.remove(item)
            }
            if (_selectedItems.isNotEmpty()) {
                onSelectionChangeListener?.invoke(_selectedItems.first())
            }
        }
    }

    override fun getItemCount(): Int {
        return languages.size
    }

    private class LanguageHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ItemLanguageBinding by viewBinding()

        var isSelected: Boolean = false
            set(value) {
                field = value
                binding.check.visibility = if (value) View.VISIBLE else View.INVISIBLE
            }

        fun bind(model: Language, isSelected: Boolean) {
            this.isSelected = isSelected
            binding.title.text = model.title
            binding.code.text = model.code
        }
    }
}