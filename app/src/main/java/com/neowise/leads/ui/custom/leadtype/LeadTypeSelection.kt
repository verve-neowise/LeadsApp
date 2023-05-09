package com.neowise.leads.ui.custom.leadtype

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.neowise.leads.R
import com.neowise.leads.domain.models.LeadIntentionType
import com.neowise.leads.util.isNotNull

class LeadTypeSelection(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatAutoCompleteTextView(context, attributeSet) {

    var items: List<LeadIntentionType> = listOf()

    private var onSelectionChangeListener: ((LeadIntentionType) -> Unit)? = null

    fun setOnSelectionChangeListener(listener: (LeadIntentionType) -> Unit) {
        this.onSelectionChangeListener = listener
    }

    private var selectedType: LeadIntentionType? = null
        set(value) {
            field = value
            updateText(value)
        }

    val selectedItem: LeadIntentionType?
        get() = selectedType

    fun select(item: LeadIntentionType?) {
        if (item == null) {
            selectedType = null
            return
        }
        val find = items.find { it.id == item.id }
        selectedType = find
    }

    init {
        inputType = InputType.TYPE_NULL
        initDialog()
    }

    private fun initDialog() {
        setOnClickListener {

            val dialog = LeadTypeSelectionDialog(items)

            dialog.setOnSelect {
                selectedType = it
                onSelectionChangeListener?.invoke(it)
            }

            showDialog(dialog)
        }
    }

    private fun showDialog(dialog: LeadTypeSelectionDialog) {
        dialog.show(findFragment<Fragment>().childFragmentManager, "Tag")
    }

    private fun updateText(value: LeadIntentionType?) {
        if (value.isNotNull()) {
            setText(value.toString())
        }
        else {
            setText("")
            hint = context.getString(R.string.unknown)
        }
    }
}