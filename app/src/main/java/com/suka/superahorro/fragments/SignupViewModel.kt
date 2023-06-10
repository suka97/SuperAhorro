package com.suka.superahorro.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.entities.User

class SignupViewModel : ViewModel() {
    private lateinit var context: Context


    fun init(context: Context) {
        this.context = context
    }


    fun signup(mail: String, pass: String): Boolean {
        return true
    }
}