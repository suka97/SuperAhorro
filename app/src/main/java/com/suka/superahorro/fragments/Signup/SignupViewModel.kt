package com.suka.superahorro.fragments.Signup

import android.content.Context
import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {
    private lateinit var context: Context


    fun init(context: Context) {
        this.context = context
    }


    fun signup(mail: String, pass: String): Boolean {
        return true
    }
}