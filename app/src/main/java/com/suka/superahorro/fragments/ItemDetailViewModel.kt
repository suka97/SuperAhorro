package com.suka.superahorro.fragments

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suka.superahorro.database.AppDatabase
import com.suka.superahorro.database.CartItemDao
import com.suka.superahorro.entities.CartItem

class ItemDetailViewModel : ViewModel() {
    private lateinit var context: Context

    private var db: AppDatabase? = null
    private var cartItemDao: CartItemDao? = null

    val cartItem = MutableLiveData<CartItem>()


    fun init(context: Context, itemID: Long) {
        this.context = context
        db = AppDatabase.getInstance(context)
        cartItemDao = db?.cartItemDao()

        cartItem.value = cartItemDao?.fetchCartItemById(itemID)!!
    }


    fun getCartItem() : CartItem {
        return cartItem.value!!
    }

    fun updateCartItem(newItem: CartItem) {
        cartItem.value = newItem
        cartItemDao?.updateCartItem(cartItem.value!!)
    }
}