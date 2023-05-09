package com.neowise.leads.util

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

fun Fragment.activityNavController(@IdRes viewId: Int): NavController {
    return requireActivity().findNavController(viewId)
}

fun Fragment.back() {
    findNavController().popBackStack()
}