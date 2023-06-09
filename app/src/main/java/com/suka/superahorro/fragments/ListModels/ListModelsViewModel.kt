package com.suka.superahorro.fragments.ListModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbModelRef
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.dbclasses.Model
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListModelsViewModel : ViewModel() {
    val dbAuthError = MutableLiveData<Boolean>(false)
    var isLoading = MutableLiveData<Boolean>(false)

    lateinit var item: Item
    lateinit var modelsList: MutableList<DbModelRef>

    fun init(item: Item, callback: ()->Unit) {
        this.item = item
        modelsList = item.data.models
        callback()
    }


    fun addNewModel(modelName: String, callback: () -> Unit) {
        viewModelScope.launch {
            var newModel = Model(modelName, item.toRef())
            isLoading.value = true
            newModel = async { Database.addModel(newModel) }.await()
            isLoading.value = false

            callback()
        }
    }
}