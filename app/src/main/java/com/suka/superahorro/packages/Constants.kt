package com.suka.superahorro.packages

const val GLOBAL_UNIT_AMOUNT = "kg"
const val GLOBAL_UNIT_PRICE = "$"

fun Float.round(decimals: Int = 2): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (kotlin.math.round(this * multiplier) / multiplier).toFloat()
}