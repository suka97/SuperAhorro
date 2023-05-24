package com.suka.superahorro.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.database.AppDatabase
import com.suka.superahorro.database.UserDao
import com.suka.superahorro.entities.User

class SignupViewModel : ViewModel() {
    private lateinit var context: Context

    private var db: AppDatabase? = null
    private var usersDao: UserDao? = null

    fun init(context: Context) {
        this.context = context
        db = AppDatabase.getInstance(context)
        usersDao = db?.userDao()
    }


    fun signup(mail: String, pass: String): Boolean {
        val existing_user = usersDao?.fetchUserByMail(mail) ?: return false
        usersDao?.insertUser(User(mail, pass))
        return true
    }
}