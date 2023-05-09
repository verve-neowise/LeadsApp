package com.neowise.leads.ui.custom.statusbar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.neowise.leads.R
import com.neowise.leads.databinding.CustomStatusBinding

class StatusIndicator(
    context: Context,
    attributeSet: AttributeSet,
) : LinearLayout(context, attributeSet) {

    private val binding: CustomStatusBinding = CustomStatusBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
        initAttributes(attributeSet)
    }

    private fun initAttributes(attributeSet: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.StatusIndicator, 0, 0
        )

        val text = typedArray.getString(R.styleable.StatusIndicator_android_text)
        val color = typedArray.getColor(
            R.styleable.StatusIndicator_si_indicatorColor,
            ContextCompat.getColor(context, R.color.purple_200)
        )

        setText(text ?: "")
        binding.indicator.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun setText(text: String) {
        binding.status.text = text
    }

    fun setColor(hexColor: String) {
        val color = Color.parseColor(hexColor)
        binding.indicator.backgroundTintList = ColorStateList.valueOf(color)
    }
}