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

    fun init(callback: ()->Unit) {
        viewModelScope.launch {
            Database.init()
            isLoading.value = true
            item = async { Database.getItem(24901) }.await()
            modelsList = item.data.models
            isLoading.value = false

            callback()
        }
    }


    fun addNewModel(modelName: String, callback: () -> Unit) {
        viewModelScope.launch {
            val newModel = Model(modelName)
            isLoading.value = true
            newModel.data.id = async { Database.addModel(newModel) }.await()
            async { Database.setModel(newModel) }.await()
            isLoading.value = false

            callback()
        }
    }
}