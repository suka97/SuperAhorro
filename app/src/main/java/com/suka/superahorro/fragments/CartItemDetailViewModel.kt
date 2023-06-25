package com.suka.superahorro.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import com.suka.superahorro.dbclasses.CartItem

class CartItemDetailViewModel : ViewModel() {
    private lateinit var context: Context

    lateinit var cartItem: CartItem


    fun init(context: Context, cartItem: CartItem) {
        this.context = context
        this.cartItem = cartItem
//        cartItem.value = cartItemDao?.fetchCartItemById(itemID)!!
    }


    fun updateCartItem(newItem: CartItem) {
//        cartItem.value = newItem
//        cartItemDao?.updateCartItem(cartItem.value!!)
    }
}