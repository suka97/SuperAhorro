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
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.Model
import com.suka.superahorro.dbclasses.User
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

object Database {
    private lateinit var user_id: String
    private lateinit var userDoc: DocumentReference
    private lateinit var cartsColl: CollectionReference
    private lateinit var itemsColl: CollectionReference
    private lateinit var modelsColl: CollectionReference

    private lateinit var storageRef: StorageReference

    fun init() {
        user_id = Firebase.auth.currentUser!!.uid
        val db = Firebase.firestore
        userDoc = db.collection("users").document(user_id)
        cartsColl = userDoc.collection("carts")
        itemsColl = userDoc.collection("items")
        modelsColl = userDoc.collection("models")

        storageRef = Firebase.storage.reference.child("users/$user_id")
    }


    suspend fun getOpenedCarts(): MutableList<Cart> {
        val req = cartsColl.whereEqualTo("status", DbCart.STATUS_OPENED).get().await()
        val dbcarts = req.toObjects(DbCart::class.java)
        return dbcarts.map { Cart(it) }.toMutableList()
    }


    suspend fun saveCart(cart: Cart) {
        cartsColl.document(cart.data.id).set(cart.data).await()
    }


    suspend fun addCart(cart: Cart): Cart {
        cart.data.id = cartsColl.add(cart.data).await().id
        saveCart(cart)
        return cart
    }


    suspend fun getUser(): User {
        val user = userDoc.get().await().toObject<DbUser>() ?: throw Exception("User not found")
        return User(user)
    }


    suspend fun addItem(item: Item) {
        itemsColl.document(item.data.id.toString()).set(item.data).await()
        userDoc.update("items", FieldValue.arrayUnion(item.data)).await()
    }


    suspend fun getItem(id: Int): Item {
        val item = itemsColl.document(id.toString()).get().await().toObject<DbItem>() ?: throw Exception("Item not found")
        return Item(item)
    }


    suspend fun getModelById(id: String): Model {
        val model = modelsColl.document(id).get().await().toObject<DbModel>() ?: throw Exception("Model not found")
        return Model(model)
    }


    suspend fun getModelBySku(sku: String): Model? {
        val model = modelsColl.whereEqualTo("sku", sku).get().await().toObjects(DbModel::class.java).firstOrNull() ?: return null
        return Model(model)
    }


    suspend fun addModel(model: Model): Model {
        model.data.id = modelsColl.add(model.data).await().id
        saveModel(model)
        if ( model.data.item_id != null ) {
            val itemId = model.data.item_id.toString()
            itemsColl.document(itemId).update("models", FieldValue.arrayUnion(model.toRef())).await()
        }
        return model
    }


    suspend fun saveModel(model: Model) {
        modelsColl.document(model.data.id).set(model.data).await()
    }


    suspend fun uploadImage(path: String, bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageRef = storageRef.child(path)
        return try {
            imageRef.putBytes(data).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }


    suspend fun deleteImage(path: String) {
        val imageRef = storageRef.child(path)
        imageRef.delete().await()
    }


    suspend fun setModelImage(modelId: String, bitmap: Bitmap): String? {
        val url = uploadImage("models/${modelId}", bitmap)
        modelsColl.document(modelId).update("img", url).await()
        return url
    }


    suspend fun deleteModelImage(modelId: String) {
        deleteImage("models/${modelId}")
        modelsColl.document(modelId).update("img", FieldValue.delete()).await()
    }
}