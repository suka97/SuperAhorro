package com.suka.superahorro.fragments.CartItemDetail

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.suka.superahorro.database.Database
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.Model
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartItemDetailViewModel : ViewModel() {
    var isLoading = MutableLiveData<Boolean>(false)
    val isInitialized = MutableLiveData<Boolean>(false)
    val dbAuthError = MutableLiveData<Boolean>(false)

    lateinit var cartItem: CartItem
    lateinit var item: Item


    fun init(cartItem: CartItem) {
        this.cartItem = cartItem
    }


    fun uploadImage(bitmap: Bitmap, callback: (url: String)->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val url = async { Database.uploadImage("models/${cartItem.data.model!!.id}", bitmap) }.await()
            isLoading.value = false
            if (url!=null) {
                cartItem.data.model!!.img = url
                callback(url)
            }
        }
    }


    fun deleteImage(callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            async { Database.deleteImage("models/${cartItem.data.model!!.id}") }.await()
            isLoading.value = false
            cartItem.data.model!!.img = null
            callback()
        }
    }


    fun getItemData(callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            item = async { Database.getItem(cartItem.data.id) }.await()
            isLoading.value = false
            callback()
        }
    }


    fun linkModel(model: Model, callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            model.data.id = async { Database.addModel(model) }.await()
            async { Database.setModel(model) }.await()
            cartItem.linkModel(model)
            isLoading.value = false

            callback()
        }
    }


    fun linkModelByName(name: String, callback: () -> Unit) {
        linkModel(Model(name), callback)
    }
}