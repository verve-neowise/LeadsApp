package com.neowise.leads.ui.custom.selection

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neowise.leads.R
import com.neowise.leads.databinding.DialogSelectionInputBinding
import com.neowise.leads.util.addTextCompleteListener

class SelectionInputDialog<T>(
    private val title: String,
    private val adapter: SelectionAdapter<T>?,
    private val enableSearch: Boolean = false,
) : BottomSheetDialogFragment(R.layout.dialog_selection_input) {

    private val binding: DialogSelectionInputBinding by viewBinding()

    private var onDismissRequest: (() -> Unit)? = null
    private var onSearchListener: ((String) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.closeIcon.setOnClickListener {
            dismiss()
        }

        setPeekHeight(view)

        binding.title.text = title

        binding.countryRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.countryRecycler.adapter = adapter

        binding.searchLayout.visibility = if (enableSearch) VISIBLE else GONE

        if (enableSearch) {
            binding.searchBar.addTextCompleteListener {
                adapter?.filter(it)
            }
        }
    }

    private fun setPeekHeight(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog

            val bottomSheet: FrameLayout = dialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            ) as FrameLayout

            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from<View>(bottomSheet)

            behavior.peekHeight = resources.displayMetrics.heightPixels/2
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissRequest?.invoke()
    }

    fun setOnDismissRequest(onRequest: () -> Unit) {
        this.onDismissRequest = onRequest
    }
}