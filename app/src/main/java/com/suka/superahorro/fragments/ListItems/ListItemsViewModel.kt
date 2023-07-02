package com.suka.superahorro.fragments.ListItems

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbItemRef
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListItemsViewModel : ViewModel() {
    val dbAuthError = MutableLiveData<Boolean>(false)
    var isLoading = MutableLiveData<Boolean>(false)

    lateinit var user: User
    lateinit var itemsList: MutableList<DbItemRef>

    fun init(callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            user = async { Database.getUser() }.await()
            itemsList = user.data.items
            isLoading.value = false

            callback()
        }
    }


    fun addNewItem(itemName: String, callback: () -> Unit) {
        viewModelScope.launch {
            val newItem = user.addNewItem(itemName)
            isLoading.value = true
            async { Database.addItem(newItem) }.await()
            isLoading.value = false

            callback()
        }
    }


    fun getItem(position: Int, callback: (item: Item)->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val item = async { Database.getItem(itemsList[position].id) }.await()
            isLoading.value = false

            callback(item)
        }
    }
}