package com.suka.superahorro.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import com.suka.superahorro.database.Database
import com.suka.superahorro.my_entities.CartItem

class CartViewModel : ViewModel() {
    private lateinit var context: Context

    var cartItems: MutableList<CartItem> = mutableListOf<CartItem>()
    var onItemsChange: (() -> Unit)? = null


    fun init(context: Context) {
        this.context = context
        cartItems = Database.getCartItems()
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