package com.suka.superahorro.packages

import android.content.Context
import android.util.AttributeSet
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView


class InstantAutoCompleteTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.autoCompleteTextViewStyle
) : AppCompatAutoCompleteTextView(context, attrs, defStyleAttr) {

    // Mostrar sugerencias tan pronto como se empiece a escribir
    override fun enoughToFilter(): Boolean {
        return true
    }
}