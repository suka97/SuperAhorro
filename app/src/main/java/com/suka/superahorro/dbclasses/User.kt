package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbItem
import com.suka.superahorro.database.DbUser
import com.suka.superahorro.packages.generateRandomString
import kotlin.random.Random

class User (var data: DbUser) {
    fun getItemsAutocomplete() : List<String> {
        return data.items.map { it.name }
    }

    fun addNewItem(name: String): Item {
        var id = Random.nextInt(from = 10000, until = 99999)
        while (data.items.any { it.id == id }) {
            id = Random.nextInt(from = 10000, until = 99999)
        }
        val newItem = Item(id, name)
        data.items.add(newItem.data)
        return newItem
    }
}