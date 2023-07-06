package com.suka.superahorro.fragments.ListUnits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbModelRef
import com.suka.superahorro.database.DbUnit
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListUnitsViewModel : ViewModel() {
    var isLoading = MutableLiveData<Boolean>(false)

    lateinit var user: User

    fun init(callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            user = async { Database.getUser() }.await()
            isLoading.value = false

            callback()
        }
    }


    fun saveUnits(callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            Database.saveUser(user)
            isLoading.value = false

            callback()
        }
    }


    fun addNewUnit(nameLong: String, callback: ()->Unit) {
        viewModelScope.launch {
            user.addNewUnit(nameLong)
            callback()
        }
    }
}