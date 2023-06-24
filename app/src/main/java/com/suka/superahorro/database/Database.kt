package com.suka.superahorro.database

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.dbclasses.CartItem
import kotlinx.coroutines.tasks.await

object Database {
    private lateinit var user_id: String
    private lateinit var userDoc: DocumentReference
    private lateinit var cartsColl: CollectionReference

    fun init() {
        user_id = Firebase.auth.currentUser!!.uid
        val db = Firebase.firestore
        userDoc = db.collection("users").document(user_id)
        cartsColl = userDoc.collection("carts")
    }

    // cartItems
    suspend fun getCart(): Cart? {
        try {
            val cart = cartsColl.document("J4WNALZhZFf78wkosMre").get().await().toObject<DbCart>() ?: throw Exception("Cart not found")
            return Cart(cart)
        }
        catch (e: Exception) {
            Log.e("Database", "Exception thrown: ${e.message}")
            return null
        }
    }


    suspend fun saveCart(cart: Cart) {
        try {
            cartsColl.document(cart.data.id).set(cart.data).await()
        }
        catch (e: Exception) {
            Log.e("Database", "Exception thrown: ${e.message}")
            return
        }
    }


    fun insertCartItem(cartItem: CartItem) {

    }


    fun removeCartItem(cartItem: CartItem) {

    }

    // user
//    fun getUser(): User {
//        return User("","")
//    }
}