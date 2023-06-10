package com.suka.superahorro.fragments

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suka.superahorro.database.AppDatabase
import com.suka.superahorro.database.CartItemDao
import com.suka.superahorro.entities.CartItem

class CartViewModel : ViewModel() {
    private lateinit var context: Context
    private lateinit var auth: FirebaseAuth

    private var db: AppDatabase? = null
    private var cartItemDao: CartItemDao? = null

    var cartItems: MutableList<CartItem> = mutableListOf<CartItem>()
    var onItemsChange: (() -> Unit)? = null


    fun init(context: Context) {
        this.context = context
        db = AppDatabase.getInstance(context)
        cartItemDao = db?.cartItemDao()
        cartItems = cartItemDao?.fetchAllCartItems() ?: mutableListOf<CartItem>()

        auth = Firebase.auth
        auth.signInWithEmailAndPassword("user@mail.com", "pass123")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("firebase", "signInWithEmail:success")
                } else {
                    Log.d("firebase", "signInWithEmail:failure")
                }
            }
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