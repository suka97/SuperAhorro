package com.suka.superahorro.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import com.suka.superahorro.dbclasses.CartItem

class CartItemDetailViewModel : ViewModel() {
    lateinit var cartItem: CartItem


    fun init(cartItem: CartItem) {
        this.cartItem = cartItem
    }
}