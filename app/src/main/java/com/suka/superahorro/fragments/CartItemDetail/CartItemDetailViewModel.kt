package com.suka.superahorro.fragments.CartItemDetail

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.Model
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartItemDetailViewModel : ViewModel() {
    interface FragmentNotifier {
        fun onItemUpdated()
    }

    var isLoading = MutableLiveData<Boolean>(false)
    val isInitialized = MutableLiveData<Boolean>(false)
    val dbAuthError = MutableLiveData<Boolean>(false)

    var itemDetail: Item? = null

    lateinit var cart: Cart
    lateinit var cartItem: CartItem
    lateinit var fragmentNotifier: FragmentNotifier


    fun init(cart: Cart, itemPos: Int, fragmentNotifier: FragmentNotifier) {
        this.cart = cart
        this.cartItem = cart.getItem(itemPos)
        this.fragmentNotifier = fragmentNotifier
    }


    fun uploadImage(bitmap: Bitmap, callback: (url: String)->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val url = async { Database.setModelImage(cartItem.data.model!!.id, bitmap) }.await()
            isLoading.value = false
            if (url!=null) {
                cartItem.data.model!!.img = url
                callback(url)
            }
        }
    }


    fun deleteImage(callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            async { Database.deleteModelImage(cartItem.data.model!!.id) }.await()
            isLoading.value = false
            cartItem.data.model!!.img = null
            callback()
        }
    }


    fun getItemData(callback: ()->Unit) {
        viewModelScope.launch {
            if ( itemDetail == null ) {
                isLoading.value = true
                itemDetail = async { Database.getItem(cartItem.data.id) }.await()
                isLoading.value = false
            }

            callback()
        }
    }


    fun linkNewModel(modelName: String) {
        val model = Model(modelName)
        model.data.item_id = cartItem.data.id

        viewModelScope.launch {
            isLoading.value = true
            val newModel = async { Database.addModel(model) }.await()
            cartItem.linkModel(newModel)
            isLoading.value = false

            fragmentNotifier.onItemUpdated()
        }
    }


    fun unlinkModel() {
        cartItem.data.model = null
    }


    fun getModelBySku(sku: String, callback: (Model?) -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val model = async { Database.getModelBySku(sku) }.await()
            isLoading.value = false

            callback(model)
        }
    }


    fun linkModelById(id: String) {
        viewModelScope.launch {
            isLoading.value = true
            val model = async { Database.getModelById(id) }.await()
            isLoading.value = false
            cartItem.linkModel(model)

            fragmentNotifier.onItemUpdated()
        }
    }


    fun saveCartChanges(callback: () -> Unit) {
        cart.setItem(cartItem)
        viewModelScope.launch {
            async { Database.saveCart(cart) }.await()
            callback()
        }
    }
}