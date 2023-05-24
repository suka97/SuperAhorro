package com.suka.superahorro.fragments

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suka.superahorro.database.AppDatabase
import com.suka.superahorro.database.CartItemDao
import com.suka.superahorro.database.UserDao
import com.suka.superahorro.entities.CartItem
import com.suka.superahorro.entities.User

class UserViewModel : ViewModel() {
    private lateinit var context: Context

    private var db: AppDatabase? = null
    private var usersDao: UserDao? = null
    lateinit var sharedPreferences: SharedPreferences

    var user = MutableLiveData<User>()


    fun init(context: Context) {
        this.context = context
        db = AppDatabase.getInstance(context)
        usersDao = db?.userDao()
        sharedPreferences = context.getSharedPreferences("com.suka.superahorro.PREFERENCES", Context.MODE_PRIVATE)

        val mail: String = sharedPreferences.getString("user_mail", "") ?:""
        val pass: String = sharedPreferences.getString("user_pass", "") ?:""
        user.value = usersDao?.fetchUserByCredentials(mail, pass)
    }


    fun clearLoginInfo() {
        sharedPreferences.edit().clear().apply()
    }


    fun getUser() : User {
        return user.value!!
    }


    fun updateUser(newUser: User) {
        user.value = newUser
        usersDao?.updateUser(user.value!!)
        sharedPreferences.edit().putString("user_pass", newUser.pass).apply()
    }
}