package com.neowise.leads.ui.custom.selection

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.neowise.leads.R
import com.neowise.leads.util.isNotNull

class SelectionInput<T>(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatAutoCompleteTextView(context, attributeSet) {

    var title: String = ""
    private var enableSearch: Boolean = false
    private var dialog: SelectionInputDialog<T>? = null

    private var onSelectionChangeListener: ((T?) -> Unit)? = null

    private var currentItem: T? = null
        set(value) {
            field = value
            updateText(value)
        }

    var selectedItem: T?
        get() = currentItem
        set(value) {
            currentItem = value
        }

    fun select(item: T) {
        adapter?.let {
            val index = it.items.indexOf(item)
            selectedItem = if (index == -1) null else it.items[index]
        }
    }

    var adapter: SelectionAdapter<T>? = null
        set(value) {
            field = value
            setupAdapter()
        }

    init {
        inputType = InputType.TYPE_NULL

        initAttributes(attributeSet)

        setOnClickListener {
            showDialog()
        }
    }

    private fun initAttributes(attributeSet: AttributeSet) {

        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.SelectionInput, 0, 0
        )

        title = typedArray.getString(R.styleable.SelectionInput_android_title) ?: ""
        enableSearch = typedArray.getBoolean(R.styleable.SelectionInput_enableSearch, false)
    }

    private fun setupAdapter() {
        adapter?.setOnItemSelectListener {

            currentItem = it

            if (adapter?.selectionMode == SelectionMode.SINGLE) {
                onSelectionChangeListener?.invoke(it)
                dialog?.dismiss()
            }
        }
    }

    private fun showDialog() {

        dialog = SelectionInputDialog(title, adapter, enableSearch).apply {

            if (adapter?.selectionMode == SelectionMode.MULTIPLE) {
                setOnDismissRequest {
                    onSelectionChangeListener?.invoke(null)
                }
            }

            show(findFragment<Fragment>().childFragmentManager, "Tag")
        }
    }

    private fun updateText(value: T?) {
        if (value.isNotNull()) {
            setText(value.toString())
        }
        else {
            setText("")
            setHint(context.getString(R.string.unknown))
        }
    }

    fun setOnSelectionChangeListener(listener: (T?) -> Unit) {
        onSelectionChangeListener = listener
    }
}