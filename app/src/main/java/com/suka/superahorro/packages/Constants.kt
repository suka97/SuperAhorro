package com.suka.superahorro.packages

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