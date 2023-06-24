package com.suka.superahorro.packages

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

const val GLOBAL_UNIT_AMOUNT = "kg"
const val GLOBAL_UNIT_PRICE = "$"

fun Float.round(decimals: Int = 2): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (kotlin.math.round(this * multiplier) / multiplier).toFloat()
}


fun Float?.toStringNull(): String {
    if (this == null) return ""
    return this.toString()
}


fun TextInputLayout.text(): String {
    return this.editText?.text.toString()
}

fun TextInputLayout.number(): Float? {
    return this.editText?.text.toString().toFloatOrNull()
}


fun generateRandomString(length: Int): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}


fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}
fun Fragment.showKeyboard(view: View) {
    view.requestFocus()
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}