package com.suka.superahorro.fragments.User

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suka.superahorro.dbclasses.User

class UserViewModel : ViewModel() {
    var isLoading = MutableLiveData<Boolean>(false)

    lateinit var user_email: String


    fun init(callback: ()->Unit) {
        user_email = Firebase.auth.currentUser?.email ?:""

        callback()
    }


    fun clearLoginInfo() {
        Firebase.auth.signOut()
    }


//    fun getUser() : User {
//        return User(""",""")
//    }


    fun updateUser(newUser: User) {

    }
}