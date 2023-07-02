package com.suka.superahorro.fragments.Login

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    interface LoginListener {
        fun onLoginSuccess()
        fun onLoginError()
    }
    var loginListener: LoginListener? = null


    fun login(email: String?=null, pass: String?=null) {
        isLoading.value = true
        val fireAuth = Firebase.auth
        if ( fireAuth.currentUser != null ) {
            loginListener?.onLoginSuccess()
            return
        }

        if ( email == null || pass == null ) {
            return
        }
        Firebase.auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = fireAuth.currentUser
                    Log.d("firebase", "signInWithEmail:success")
                    loginListener?.onLoginSuccess()
                } else {
                    isLoading.value = false
                    Log.d("firebase", "signInWithEmail:failure")
                    loginListener?.onLoginError()
                }
            }
    }

}