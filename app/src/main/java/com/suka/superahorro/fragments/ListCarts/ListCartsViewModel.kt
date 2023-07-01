package com.suka.superahorro.fragments.ListCarts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.dbclasses.Cart
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListCartsViewModel : ViewModel() {
    val dbAuthError = MutableLiveData<Boolean>(false)
    var isLoading = MutableLiveData<Boolean>(false)

    lateinit var carts: MutableList<Cart>

    fun init(callback: ()->Unit) {
        viewModelScope.launch {
            Database.init()
            isLoading.value = true
            carts = async { Database.getOpenedCarts() }.await()
            isLoading.value = false

            callback()
        }
    }


    fun addNewCart(shopName: String, callback: () -> Unit) {
        viewModelScope.launch {
            var newCart = Cart(shopName)
            isLoading.value = true
            newCart = async { Database.addCart(newCart) }.await()
            isLoading.value = false
            carts.add(newCart)

            callback()
        }
    }

}