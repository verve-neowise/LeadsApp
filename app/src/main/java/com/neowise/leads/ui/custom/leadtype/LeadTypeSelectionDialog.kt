package com.neowise.leads.ui.custom.leadtype

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neowise.leads.R
import com.neowise.leads.databinding.DialogLeadTypeBinding
import com.neowise.leads.domain.models.LeadIntentionType

class LeadTypeSelectionDialog(
    private val types: List<LeadIntentionType>,
) : BottomSheetDialogFragment(R.layout.dialog_lead_type) {

    private val binding: DialogLeadTypeBinding by viewBinding()

    private var onSelect: ((LeadIntentionType) -> Unit)? = null

    fun setOnSelect(listener: (LeadIntentionType) -> Unit) {
        this.onSelect = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.setText(R.string.lead_type)

        binding.closeIcon.setOnClickListener {
            dismiss()
        }

        render(types)

        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            val type = types.find { it.id == id }
            onSelect?.invoke(type!!)
        }
    }

    private fun render(types: List<LeadIntentionType>) {
        types.forEach {
            binding.radioGroup.addView(
                RadioButton(context).apply {
                    id = it.id
                    text = it.title
                },
                0
            )
        }
    }
}