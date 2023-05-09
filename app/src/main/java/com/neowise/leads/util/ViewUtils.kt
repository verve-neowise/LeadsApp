package com.neowise.leads.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.neowise.leads.R
import com.squareup.picasso.Picasso

fun TextView.showIfExists(value: String?) {
    text = value
    visibility = if (value != null) View.VISIBLE else View.GONE
}

fun EditText.string(): String {
    return text.toString().trim()
}

fun ImageView.load(url: String?) {
    val picasso = Picasso.get()
    picasso.isLoggingEnabled = true
    picasso.load(url)
        .placeholder(R.drawable.lead_profile_placeholder)
        .into(this)
}

fun validate(editText: EditText, condition: () -> Boolean): Boolean {
    return true.validate(editText, condition)
}

fun Boolean.validate(editText: EditText, condition: () -> Boolean): Boolean {
    val check = condition()
    if (!check) {
        editText.error = ""
    }
    else {
        editText.error = null
    }
    return this && check
}

fun TextInputEditText.addTextCompleteListener(listener: (text: String) -> Unit) {
    addTextChangedListener(object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            listener(s.toString())
        }
    })
}