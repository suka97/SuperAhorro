package com.suka.superahorro.fragments.Signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    enum class SignupResult {
        SUCCESS, MAIL_EXISTS, MAIL_INVALID, PASS_INVALID, UNDEFINED
    }

    fun signup(mail: String, pass: String, callBack: (SignupResult)->Unit) {
        val authMail = mail.trim()
        val authPass = pass.trim()
        isLoading.value = true
        viewModelScope.launch {
            Firebase.auth.createUserWithEmailAndPassword(authMail, authPass)
                .addOnCompleteListener { task ->
                    var result = SignupResult.UNDEFINED
                    if ( task.isSuccessful ) result = SignupResult.SUCCESS
                    else {
                        val exc = task.exception
                        if (exc is FirebaseAuthWeakPasswordException) result = SignupResult.PASS_INVALID
                        else if (exc is FirebaseAuthInvalidCredentialsException) result = SignupResult.MAIL_INVALID
                        else if (exc is FirebaseAuthUserCollisionException) result = SignupResult.MAIL_EXISTS

                        isLoading.value = false
                    }
                    callBack(result)
                }
        }
    }
}