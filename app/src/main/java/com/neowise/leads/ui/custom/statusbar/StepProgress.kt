package com.neowise.leads.ui.custom.statusbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.neowise.leads.R

class StepProgress(
    context: Context,
    attributeSet: AttributeSet
) : LinearLayout(context, attributeSet) {

    private val layoutInflater = LayoutInflater.from(context)

    private val stepViews: MutableList<View> = mutableListOf()

    private var maxStep = 0
    private var step = 0

    init {
        initLayout()
        initAttributes(attributeSet)
    }

    fun updateStep(step: Int, max: Int) {
        this.step = step
        this.maxStep = max
        render()
    }

    private fun initLayout() {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        orientation = HORIZONTAL
        dividerDrawable = ContextCompat.getDrawable(context, R.drawable.shape_steps_divider)
        showDividers = SHOW_DIVIDER_MIDDLE
    }

    private fun initAttributes(attributeSet: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.StepProgress, 0, 0
        )

        val maxStep = typedArray.getInteger(R.styleable.StepProgress_st_max_step, 0)
        val step = typedArray.getInteger(R.styleable.StepProgress_st_step, 0)

        updateStep(step, maxStep)
    }

    private fun render() {
        val count = maxStep - stepViews.size
        if (count > 0) {
            repeat(count) {
                stepViews.add(inflateStepView())
            }
            rebuildViews()
        }
        updateIndications()
    }

    private fun rebuildViews() {
        removeAllViews()

        for (i in 0 until maxStep) {
            addView(stepViews[i])
        }
    }

    private fun updateIndications() {
        stepViews.forEachIndexed { index, view ->
            view.isActivated = index < step
        }
    }

    private fun inflateStepView(): View {
        return layoutInflater
            .inflate(R.layout.custom_step_indicator, this, false)
    }
}