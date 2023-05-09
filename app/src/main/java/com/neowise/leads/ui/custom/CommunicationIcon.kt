package com.neowise.leads.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.neowise.leads.R
import com.neowise.leads.databinding.CustomItemCommunicationBinding

class CommunicationIcon(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    private val binding = CustomItemCommunicationBinding.inflate(
        LayoutInflater.from(context)
    )

    init {
        addView(binding.root)
        initAttributes(attributeSet)
    }

    private fun initAttributes(attributeSet: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.CommunicationIcon, 0, 0
        )

        val icon = typedArray.getDrawable(R.styleable.CommunicationIcon_ci_icon)
        val text = typedArray.getString(R.styleable.CommunicationIcon_ci_text)
        val enabled = typedArray.getBoolean(R.styleable.CommunicationIcon_ci_enabled, true)

        binding.icon.setImageDrawable(icon)
        binding.text.text = text

        isEnabled = enabled
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.iconBackground.isEnabled = enabled
        binding.text.isEnabled = enabled
    }
}