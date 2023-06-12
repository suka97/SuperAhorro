package com.suka.superahorro.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.my_entities.CartItem
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private lateinit var context: Context

    var cartItems: MutableList<CartItem> = mutableListOf<CartItem>()
    var onItemsChange: (() -> Unit)? = null


    fun init(context: Context) {
        this.context = context
        Database.init()
        viewModelScope.launch {
            cartItems = async { Database.getCartItems() }.await()
        }
    }


    fun updateItems() {
//        cartItems = cartItemDao?.fetchAllCartItems() ?: mutableListOf<CartItem>()
//        onItemsChange?.invoke()
    }


    fun insertCartItem(cartItem: CartItem) {
        Database.insertCartItem(cartItem)
        onItemsChange?.invoke()
    }


    fun deleteCartItem(cartItem: CartItem) {
        Database.removeCartItem(cartItem)
        onItemsChange?.invoke()
    }
}