package com.suka.superahorro.fragments.CartHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.dbclasses.Cart
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartHistoryViewModel : ViewModel() {
    var isLoading = MutableLiveData<Boolean>(false)

    lateinit var carts: MutableList<Cart>

    fun init(callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            carts = async { Database.getClosedCarts() }.await()
            isLoading.value = false

            callback()
        }
    }

}