package com.suka.superahorro.fragments.Login

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    enum class LoginResult {
        SUCCESS, INVALID_CREDENTIALS, UNDEFINED
    }
    lateinit var loginListener: (LoginResult)->Unit


    fun login(email: String?=null, pass: String?=null) {
        isLoading.value = true
        val fireAuth = Firebase.auth
        if ( fireAuth.currentUser != null ) {
            loginListener(LoginResult.SUCCESS)
            return
        }

        if ( email == null || pass == null ) {
            isLoading.value = false
            return
        }
        val authEmail = email.trim()
        val authPass = pass.trim()
        Firebase.auth.signInWithEmailAndPassword(authEmail, authPass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = fireAuth.currentUser
                    Log.d("firebase", "signInWithEmail:success")
                    loginListener(LoginResult.SUCCESS)
                } else {
                    isLoading.value = false
                    Log.d("firebase", "signInWithEmail:failure")
                    loginListener(LoginResult.INVALID_CREDENTIALS)
                }
            }
    }

}