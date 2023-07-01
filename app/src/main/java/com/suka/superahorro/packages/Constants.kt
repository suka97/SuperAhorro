package com.suka.superahorro.packages

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout


const val GLOBAL_UNIT_AMOUNT = "kg"
const val GLOBAL_UNIT_PRICE = "$"

// activity requests
const val REQUEST_IMAGE_CAPTURE = 1


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


fun Fragment.requestImage() {
    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    } else {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
    }
}


fun Fragment.setToolbarTitle(title: String) {
    val activity = requireActivity() as AppCompatActivity
    activity.supportActionBar?.setTitle(title)
}