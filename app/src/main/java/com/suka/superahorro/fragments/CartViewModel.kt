package com.suka.superahorro.fragments

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
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
    var isLoading = MutableLiveData<Boolean>(false)


    fun init(context: Context, cartCallback: ()->Unit) {
        this.context = context
        Database.init()
        viewModelScope.launch {
            isLoading.value = true
            cart = async { Database.getCart()!! }.await()
            isLoading.value = false
            cartCallback()
        }
    }


    fun saveCartChanges() {
        viewModelScope.launch {
            isLoading.value = true
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