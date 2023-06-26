package com.suka.superahorro.fragments

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    val isInitialized = MutableLiveData<Boolean>(false)

//    var cartItems: MutableList<CartItem> = mutableListOf<CartItem>()
    var onItemsChange: (() -> Unit)? = null
    lateinit var cart: Cart
    lateinit var user: User
    var isLoading = MutableLiveData<Boolean>(false)


    init {
        Database.init()
        viewModelScope.launch {
            isLoading.value = true
            cart = async { Database.getCart() }.await()
            user = async { Database.getUser() }.await()
            isLoading.value = false

            isInitialized.value = true
        }
    }


    fun addNewItem(name: String, callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val newItem = user.addNewItem(name)
            async { Database.addItem(newItem) }.await()
            isLoading.value = false
            callback()
        }
    }


    fun saveCartChanges(showLoading: Boolean = false) {
        viewModelScope.launch {
            if (showLoading) isLoading.value = true
            async { Database.saveCart(cart) }.await()
            isLoading.value = false
        }
    }


    fun updateItems() {
//        cartItems = cartItemDao?.fetchAllCartItems() ?: mutableListOf<CartItem>()
//        onItemsChange?.invoke()
    }


    fun newCartItem(name: String): CartItem {
        cart.insertItem(CartItem(name))
        return cart.getLastItem()
    }


    fun deleteCartItem(position: Int) {
        cart.deleteItem(position)
        saveCartChanges()
        onItemsChange?.invoke()
    }
}