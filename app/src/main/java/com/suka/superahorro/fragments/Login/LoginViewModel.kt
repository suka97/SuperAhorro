package com.suka.superahorro.fragments.Login

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    private lateinit var context: Context

    val isLoading = MutableLiveData<Boolean>(false)

    interface LoginListener {
        fun onLoginSuccess()
        fun onLoginError()
    }
    var loginListener: LoginListener? = null


    fun init(context: Context) {
        this.context = context
    }


    fun login(email: String?=null, pass: String?=null) {
        val fireAuth = Firebase.auth
        if ( fireAuth.currentUser != null ) {
            loginListener?.onLoginSuccess()
            return
        }

        if ( email == null || pass == null ) {
            return
        }
        isLoading.value = true
        Firebase.auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    val user = fireAuth.currentUser
                    Log.d("firebase", "signInWithEmail:success")
                    loginListener?.onLoginSuccess()
                } else {
                    Log.d("firebase", "signInWithEmail:failure")
                    loginListener?.onLoginError()
                }
            }
    }

}