package com.suka.superahorro.fragments

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.database.AppDatabase
import com.suka.superahorro.database.UserDao

class LoginViewModel : ViewModel() {
    private lateinit var context: Context

    private var db: AppDatabase? = null
    private var usersDao: UserDao? = null
    private lateinit var sharedPreferences: SharedPreferences


    fun init(context: Context) {
        this.context = context
        db = AppDatabase.getInstance(context)
        usersDao = db?.userDao()
        sharedPreferences = context.getSharedPreferences("com.suka.superahorro.PREFERENCES", Context.MODE_PRIVATE)
    }


    fun validPreviuousUserExists(): Boolean {
        val prefMail: String = sharedPreferences.getString("user_mail", "") ?:""
        val prefPass: String = sharedPreferences.getString("user_pass", "") ?:""
        val prefUser = usersDao?.fetchUserByCredentials(prefMail, prefPass)
        return prefUser != null
    }


    fun login(mail: String, pass: String): Boolean {
        val user = usersDao?.fetchUserByCredentials(mail, pass) ?: return false
        val prefEditor = sharedPreferences.edit()
        prefEditor.putString("user_mail", mail)
        prefEditor.putString("user_pass", pass)
        prefEditor.apply()
        return true
    }
}