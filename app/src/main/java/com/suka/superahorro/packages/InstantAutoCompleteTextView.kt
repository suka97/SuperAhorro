package com.suka.superahorro.packages

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
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


    // mostrar lista on focus
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: android.graphics.Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            // Mostrar sugerencias cuando el TextView obtenga el foco
            showDropDown()
        }
    }


    // clear focus on back pressed
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        clearFocus()
        return super.onKeyPreIme(keyCode, event)
    }
}