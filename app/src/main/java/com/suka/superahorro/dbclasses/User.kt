package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbUser
import kotlin.random.Random

class User (var data: DbUser) {

    fun addNewItem(name: String): Item {
        var id = Random.nextInt(from = 10000, until = 99999)
        while (data.items.any { it.id == id }) {
            id = Random.nextInt(from = 10000, until = 99999)
        }
        val newItem = Item(id=id, name=name)
        data.items.add(newItem.toRef())
        return newItem
    }
}