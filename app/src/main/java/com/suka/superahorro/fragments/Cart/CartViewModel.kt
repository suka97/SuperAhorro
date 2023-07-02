package com.suka.superahorro.fragments.Cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbItemRef
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.User
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    val isInitialized = MutableLiveData<Boolean>(false)
    val dbAuthError = MutableLiveData<Boolean>(false)
    var isLoading = MutableLiveData<Boolean>(false)

//    var cartItems: MutableList<CartItem> = mutableListOf<CartItem>()
    var onItemsChange: (() -> Unit)? = null
    lateinit var cart: Cart
    lateinit var user: User

    // handle courritine exceptions
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("CartViewModel", "Exception in couroutine: ${throwable.message}")
        dbAuthError.value = true
    }
    private val viewModelScope = CoroutineScope(Dispatchers.Main + exceptionHandler)


    fun init(cart: Cart) {
        this.cart = cart
        viewModelScope.launch {
            isLoading.value = true
            user = async { Database.getUser() }.await()
            isLoading.value = false

            isInitialized.value = true
        }
    }


    fun addNewItem(name: String, callback: (Item)->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val newItem = user.addNewItem(name)
            async { Database.addItem(newItem) }.await()
            isLoading.value = false
            callback(newItem)
        }
    }


    fun addItemBySku(sku: String, callback: (newPos: Int?)->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val model = async { Database.getModelBySku(sku) }.await()
            isLoading.value = false

            if ( model != null ) {
                var newCartItem = CartItem(model.data.item)
                newCartItem.data.model = model.toCartRef()
                cart.insertItem(newCartItem)
                callback(cart.data.items.lastIndex)
            }
            else callback(null)
        }
    }


    fun saveCartChanges(showLoading: Boolean = false) {
        viewModelScope.launch {
            if (showLoading) isLoading.value = true
            async { Database.saveCart(cart) }.await()
            isLoading.value = false
        }
    }


    fun insertCartItem(item: DbItemRef): CartItem {
        cart.insertItem(CartItem(item))
        return cart.getLastItem()
    }


    fun deleteCartItem(position: Int) {
        cart.deleteItem(position)
        saveCartChanges()
        onItemsChange?.invoke()
    }


    fun closeCart(callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            async { cart.close() }.await()
            isLoading.value = false

            callback()
        }
    }
}