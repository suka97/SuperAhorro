package com.suka.superahorro.fragments.Login

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suka.superahorro.database.Database
import com.suka.superahorro.dbclasses.DbGlobals
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    enum class LoginResult {
        SUCCESS, INVALID_CREDENTIALS, UNDEFINED, EMAIL_NOT_VERIFIED, NOP
    }
    lateinit var loginListener: (LoginResult)->Unit


    fun login(email: String?=null, pass: String?=null) {
        isLoading.value = true
        val fireAuth = Firebase.auth
        if ( fireAuth.currentUser != null ) {
            loginSuccess(); return
        }

        if ( email == null || pass == null ) {
            loginError(LoginResult.NOP); return
        }
        val authEmail = email.trim()
        val authPass = pass.trim()
        Firebase.auth.signInWithEmailAndPassword(authEmail, authPass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("firebase", "signInWithEmail:success")
                    loginSuccess()
                } else {
                    Log.d("firebase", "signInWithEmail:failure")
                    loginError(LoginResult.INVALID_CREDENTIALS)
                }
            }
    }


    private fun loginError(result: LoginResult) {
        Firebase.auth.signOut()
        isLoading.value = false
        loginListener(result)
    }


    private fun loginSuccess() {
        if ( !Firebase.auth.currentUser!!.isEmailVerified ) {
            loginError(LoginResult.EMAIL_NOT_VERIFIED); return
        }

        viewModelScope.launch {
            Database.init()
            async { DbGlobals.init() }.await()

            // isLoading.value = false
            loginListener(LoginResult.SUCCESS)
        }
    }


    var sendingResetEmail = false
    fun resetPassword(email: String, callback: ()->Unit) {
        if ( sendingResetEmail ) return
        sendingResetEmail = true
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                sendingResetEmail = false
                if (task.isSuccessful) callback()
            }
    }

}