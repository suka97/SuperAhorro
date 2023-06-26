package com.suka.superahorro.fragments

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.suka.superahorro.database.Database
import com.suka.superahorro.dbclasses.CartItem
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartItemDetailViewModel : ViewModel() {
    var isLoading = MutableLiveData<Boolean>(false)
    val isInitialized = MutableLiveData<Boolean>(false)
    val dbAuthError = MutableLiveData<Boolean>(false)

    lateinit var cartItem: CartItem


    fun init(cartItem: CartItem) {
        this.cartItem = cartItem
    }


    fun uploadImage(bitmap: Bitmap, callback: (url: String)->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val url = async { Database.uploadImage(bitmap) }.await()
            isLoading.value = false
            if (url!=null) callback(url)
        }
    }
}