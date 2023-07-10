package com.suka.superahorro.fragments.CartItemDetail

import android.graphics.Bitmap
import android.provider.ContactsContract.Data
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbItemRef
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
    var isImgLoading = MutableLiveData<Boolean>(false)
    val isInitialized = MutableLiveData<Boolean>(false)
    val dbAuthError = MutableLiveData<Boolean>(false)

    var itemDetail: Item? = null
    var itemChanges: Boolean = false
    var modelChanges: Boolean = false

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
            isImgLoading.value = true
            val url = async { Database.setModelImage(cartItem.data.model!!.id, bitmap) }.await()
//            isImgLoading.value = false
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


    fun linkNewModel(modelName: String, sku: String?=null) {
        val model = Model(modelName, cartItem.toItemRef())
        model.data.sku = sku
        model.data.item = cartItem.toItemRef()

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
        fragmentNotifier.onItemUpdated()
    }


    fun getModelBySku(sku: String, callback: (Model?) -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val model = async { Database.getModelBySku(sku) }.await()
            isLoading.value = false

            callback(model)
        }
    }


    fun refreshModel() {
        viewModelScope.launch {
            isLoading.value = true
            val model = async { Database.getModelById(cartItem.data.model!!.id) }.await()
            isLoading.value = false
            cartItem.linkModel(model)

            fragmentNotifier.onItemUpdated()
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
        viewModelScope.launch {
            if (itemChanges) {
                cart.setItem(cartItem)
                isLoading.value = true
                async { Database.saveCart(cart) }.await()
            }
            if (modelChanges && cartItem.data.model!=null) {
                isLoading.value = true
                if (itemDetail==null) itemDetail = async { Database.getItem(cartItem.data.id) }.await()
                val cartModel = cartItem.data.model!!
                val model = async { Database.getModelById(cartItem.data.model!!.id) }.await()
                model.data.name = cartModel.name
                model.data.sku = cartModel.sku
                model.data.content = cartModel.content
                model.data.brand = cartModel.brand
                model.data.sale_mode = cartModel.sale_mode
                model.data.note = cartModel.note
                async { Database.saveModel(model, itemDetail!!) }.await()
            }

            isLoading.value = false
            itemChanges = false
            modelChanges = false
            callback()
        }
    }
}