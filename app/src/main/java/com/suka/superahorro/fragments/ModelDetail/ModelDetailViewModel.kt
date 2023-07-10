package com.suka.superahorro.fragments.ModelDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbCartItem
import com.suka.superahorro.database.DbItemRef
import com.suka.superahorro.database.DbModelRef
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.Model
import com.suka.superahorro.dbclasses.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ModelDetailViewModel : ViewModel() {
    var isLoading = MutableLiveData<Boolean>(false)
    var isImgLoading = MutableLiveData<Boolean>(false)

    var modelChanges: Boolean = false

    lateinit var model: Model
    lateinit var user: User
    lateinit var parentItem: Item


    fun init(modelRef: DbModelRef, parentItemRef: DbItemRef, callback: ()->Unit) {
        viewModelScope.launch {
            isLoading.value = true
            user = async { Database.getUser() }.await()
            model = async { Database.getModelById(modelRef.id) }.await()
            parentItem = async { Database.getItem(parentItemRef.id) }.await()
            isLoading.value = false

            callback()
        }
    }


    fun saveModel(callback: ()->Unit) {
        viewModelScope.launch {
            if (modelChanges) {
                isLoading.value = true
                async { Database.saveModel(model, parentItem) }.await()
                isLoading.value = false
            }

            modelChanges = false
            callback()
        }
    }
}