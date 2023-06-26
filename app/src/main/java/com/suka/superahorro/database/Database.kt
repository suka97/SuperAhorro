package com.suka.superahorro.database

import android.graphics.Bitmap
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.User
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

object Database {
    private lateinit var user_id: String
    private lateinit var userDoc: DocumentReference
    private lateinit var cartsColl: CollectionReference

    private lateinit var storageRef: StorageReference

    fun init() {
        user_id = Firebase.auth.currentUser!!.uid
        val db = Firebase.firestore
        userDoc = db.collection("users").document(user_id)
        cartsColl = userDoc.collection("carts")

        storageRef = Firebase.storage.reference.child("users/$user_id")
    }

    // cartItems
    suspend fun getCart(): Cart {
        val cart = cartsColl.document("J4WNALZhZFf78wkosMre").get().await().toObject<DbCart>() ?: throw Exception("Cart not found")
        return Cart(cart)
    }


    suspend fun saveCart(cart: Cart) {
        cartsColl.document(cart.data.id).set(cart.data).await()
    }


    fun insertCartItem(cartItem: CartItem) {

    }


    fun removeCartItem(cartItem: CartItem) {

    }

    suspend fun getUser(): User {
        val user = userDoc.get().await().toObject<DbUser>() ?: throw Exception("User not found")
        return User(user)
    }

    suspend fun addItem(item: Item) {
        userDoc.update("items", FieldValue.arrayUnion(item.data)).await()
    }


    suspend fun uploadImage(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageRef = storageRef.child("models/${bitmap.hashCode()}")
        var uploadTask = imageRef.putBytes(data).await()
        var url: String? = null
        if (uploadTask.task.isSuccessful) url = imageRef.downloadUrl.await().toString()
        return url
    }
}