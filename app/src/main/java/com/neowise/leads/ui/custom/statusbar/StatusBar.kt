package com.neowise.leads.ui.custom.statusbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.neowise.leads.R
import com.neowise.leads.databinding.CustomLeadStatusBinding
import com.neowise.leads.domain.models.LeadStatus
import com.neowise.leads.ui.custom.selection.SelectionAdapter
import com.neowise.leads.ui.custom.selection.SelectionInputDialog
import com.neowise.leads.ui.custom.selection.SelectionMode

/**
 * Lead status bar component, for change and view status
 */
class StatusBar(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    private val binding = CustomLeadStatusBinding.inflate(LayoutInflater.from(context))
    private var dialog: SelectionInputDialog<LeadStatus>? = null
    private var onSelectionChangeListener: ((LeadStatus) -> Unit)? = null

    /**
     * set/get Dialog title
     */
    var title: String = ""

    /**
     * get current selected items, if no selection returns null
     */
    val selectedItem: LeadStatus?
        get() = currentItem

    var adapter: SelectionAdapter<LeadStatus>? = null
        set(value) {
            field = value
            setupAdapter()
        }

    init {
        addView(binding.root)
        initAttributes(attributeSet)

        binding.statusIndicator.setOnClickListener {
            showDialog()
        }
    }

    /**
     * Setting current status
     * @param status - current Status
     */
    fun setStatus(status: LeadStatus?) {
        currentItem = status
        binding.statusIndicator.setText(status?.title ?: "")
        binding.statusIndicator.setColor(status?.color ?: "#000000")
        binding.stepProgress.updateStep(status?.step ?: 0, status?.stepsCount ?: 0)
    }

    /**
     * Setting callback for get change updates
     */
    fun setOnSelectionChangeListener(listener: (LeadStatus) -> Unit) {
        this.onSelectionChangeListener = listener
    }

    private fun showDialog() {
        dialog = SelectionInputDialog(title, adapter).apply {
            show(findFragment<Fragment>().childFragmentManager, "Tag")
        }
    }

    private fun initAttributes(attributeSet: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.StatusBar, 0, 0
        )

        title = typedArray.getString(R.styleable.StatusBar_android_title) ?: ""
    }

    private var currentItem: LeadStatus? = null
        set(value) {
            field = value
            onSelectionChangeListener?.invoke(value!!)
        }

    private fun setupAdapter() {
        adapter?.setOnItemSelectListener {
            setStatus(it)
            if (adapter?.selectionMode == SelectionMode.SINGLE) {
                dialog?.dismiss()
            }
        }
    }
}