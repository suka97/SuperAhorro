package com.suka.superahorro.dbclasses

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbUser
import kotlinx.coroutines.tasks.await

object DbGlobals {
    private lateinit var userDoc: DocumentReference

    lateinit var user: User


    suspend fun init() {
        val user_id = Firebase.auth.currentUser!!.uid
        val db = Firebase.firestore
        userDoc = db.collection("users").document(user_id)

        val dbUser = userDoc.get().await().toObject<DbUser>() ?: throw Exception("User not found")
        user = User(dbUser)
    }
}