package com.suka.superahorro.fragments

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.dbclasses.CartItem
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private lateinit var context: Context

//    var cartItems: MutableList<CartItem> = mutableListOf<CartItem>()
    var onItemsChange: (() -> Unit)? = null
    lateinit var cart: Cart


    fun init(context: Context, cartCallback: ()->Unit) {
        this.context = context
        Database.init()
        viewModelScope.launch {
            cart = async { Database.getCart()!! }.await()
            Log.d("CartViewModel", "Cart: ${cart.data}")
            cartCallback()
        }
    }


    fun saveCartChanges() {
        viewModelScope.launch {
            Database.saveCart(cart)
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