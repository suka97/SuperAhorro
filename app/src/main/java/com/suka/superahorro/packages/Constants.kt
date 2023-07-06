package com.suka.superahorro.packages

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.stfalcon.imageviewer.StfalconImageViewer
import com.suka.superahorro.R
import com.suka.superahorro.activities.MainActivity


const val GLOBAL_UNIT_AMOUNT = "kg"
const val GLOBAL_UNIT_PRICE = "$"
const val GLOBAL_UNIT_NAME = "u"

// activity requests
const val REQUEST_IMAGE_CAPTURE = 1


fun Float.round(decimals: Int = 2): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (kotlin.math.round(this * multiplier) / multiplier).toFloat()
}


fun Float?.toStringNull(): String {
    if (this == null) return ""
    return this.round(2).toString()
}


fun TextInputLayout.text(): String {
    return this.textOrNull() ?: ""
}

fun TextInputLayout.textOrNull(): String? {
    return this.editText?.text?.toString()
}

fun TextInputLayout.number(): Float {
    return this.numberOrNull() ?: 0f
}

fun TextInputLayout.numberOrNull(): Float? {
    return this.textOrNull()?.toFloatOrNull()
}

fun TextInputLayout.setText(text: String?) {
    this.editText?.setText(text)
}

fun TextInputLayout.setNumber(number: Float?) {
    this.setText(number?.toStringNull())
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


interface LoadingListener {
    fun setLoading(isLoading: Boolean)
}
fun Fragment.setLoading(isLoading: Boolean) {
    val activity = requireActivity() as LoadingListener
    activity.setLoading(isLoading)
}


fun setGlidePicture(imgView: ImageView, url: String?, placeholder: Int, onNullClick: ((View)->Unit)? = null, onSuccess: (()->Unit)? = null, onError: ((GlideException?)->Unit)? = null) {
    if ( url == null ) {
        imgView.setImageResource(R.drawable.default_item)
        imgView.setOnClickListener(onNullClick)
    }
    else {
        Glide.with(imgView.context)
            .load(url)
            .placeholder(R.drawable.default_item)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    if (onError != null) onError(e)
                    return true
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    if (onSuccess != null) onSuccess()
                    return false
                }
            })
            .into(imgView)

        imgView.setOnClickListener {
            val images = listOf(url)
            StfalconImageViewer.Builder<String>(imgView.context, images) { view: ImageView, image: String ->
                Glide.with(view.context).load(image).into(view)
            }.show()
        }
    }
}