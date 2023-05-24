package com.suka.superahorro.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import com.suka.superahorro.database.AppDatabase
import com.suka.superahorro.database.CartItemDao
import com.suka.superahorro.entities.CartItem

class CartViewModel : ViewModel() {
    private lateinit var context: Context

    private var db: AppDatabase? = null
    private var cartItemDao: CartItemDao? = null

    var cartItems: MutableList<CartItem> = mutableListOf<CartItem>()
    var onItemsChange: (() -> Unit)? = null


    fun init(context: Context) {
        this.context = context
        db = AppDatabase.getInstance(context)
        cartItemDao = db?.cartItemDao()
        cartItems = cartItemDao?.fetchAllCartItems() ?: mutableListOf<CartItem>()
    }


    fun updateItems() {
        cartItems = cartItemDao?.fetchAllCartItems() ?: mutableListOf<CartItem>()
        onItemsChange?.invoke()
    }


    fun insertCartItem(cartItem: CartItem) {
        cartItemDao?.insertCartItem(cartItem)
        onItemsChange?.invoke()
    }


    fun deleteCartItem(cartItem: CartItem) {
        cartItemDao?.deleteCartItem(cartItem)
        onItemsChange?.invoke()
    }
}