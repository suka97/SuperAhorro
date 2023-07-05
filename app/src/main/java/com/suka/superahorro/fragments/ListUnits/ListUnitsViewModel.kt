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
    lateinit var units: MutableList<DbUnit>

    fun init(callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            user = async { Database.getUser() }.await()
            units = user.data.units
            isLoading.value = false

            callback()
        }
    }
}