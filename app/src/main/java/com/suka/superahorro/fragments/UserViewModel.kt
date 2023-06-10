package com.suka.superahorro.fragments

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suka.superahorro.database.AppDatabase
import com.suka.superahorro.database.CartItemDao
import com.suka.superahorro.database.UserDao
import com.suka.superahorro.entities.CartItem
import com.suka.superahorro.entities.User

class UserViewModel : ViewModel() {
    private lateinit var context: Context

    var user = MutableLiveData<User>()


    fun init(context: Context) {
        this.context = context

        val mail: String = Firebase.auth.currentUser?.email ?:""
//        val pass: String = Firebase.auth.currentUser?.uid ?:""
        user.value = User(mail, "")
    }


    fun clearLoginInfo() {
        Firebase.auth.signOut()
    }


    fun getUser() : User {
        return User("","")
    }


    fun updateUser(newUser: User) {

    }
}