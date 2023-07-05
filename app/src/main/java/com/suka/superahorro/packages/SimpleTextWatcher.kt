package com.suka.superahorro.packages

import android.text.Editable
import android.text.TextWatcher

class SimpleTextWatcher(private val callback: (String) -> Unit) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        callback(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No es necesario implementar este método
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No es necesario implementar este método
    }
}
