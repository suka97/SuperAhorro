package com.suka.superahorro.database

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.suka.superahorro.my_entities.CartItem
import com.suka.superahorro.my_entities.User
import kotlinx.coroutines.tasks.await

object Database {
    private lateinit var user_id: String
    private lateinit var userDoc: DocumentReference

    fun init() {
        user_id = Firebase.auth.currentUser!!.uid
        val db = Firebase.firestore
        userDoc = db.collection("users").document(user_id)
    }

    // cartItems
    suspend fun getCartItems(): MutableList<CartItem> {
        try {
            val doc = userDoc.get().await()
            val items = doc!!.reference.collection("items").get().await().documents
//                .addOnSuccessListener {
//                    val items = it.reference.collection("items").get().await()
//                }
            return mutableListOf<CartItem>()
        }
        catch (e: Exception) {
            Log.e("Database", "Exception thrown: ${e.message}")
            return mutableListOf<CartItem>()
        }
    }
    fun insertCartItem(cartItem: CartItem) {

    }
    fun removeCartItem(cartItem: CartItem) {

    }

    // user
    fun getUser(): User {
        return User("","")
    }
}