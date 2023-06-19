package com.suka.superahorro.fragments

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suka.superahorro.dbclasses.CartItem

class ItemDetailViewModel : ViewModel() {
    private lateinit var context: Context

    val cartItem = MutableLiveData<CartItem>()


    fun init(context: Context, itemID: Long) {
        this.context = context

//        cartItem.value = cartItemDao?.fetchCartItemById(itemID)!!
    }


    fun getCartItem() : CartItem {
        return cartItem.value!!
    }

    fun updateCartItem(newItem: CartItem) {
        cartItem.value = newItem
//        cartItemDao?.updateCartItem(cartItem.value!!)
    }
}