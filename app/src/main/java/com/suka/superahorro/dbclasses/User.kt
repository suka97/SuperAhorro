package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbUnit
import com.suka.superahorro.database.DbUser
import kotlin.random.Random

class User (var data: DbUser) {
    constructor(id: String) : this(DbUser(id=id))

    fun addNewItem(name: String): Item {
        var id = Random.nextInt(from = 10000, until = 99999)
        while (data.items.any { it.id == id }) {
            id = Random.nextInt(from = 10000, until = 99999)
        }
        val newItem = Item(id=id, name=name)
        data.items.add(newItem.toRef())
        return newItem
    }


    fun getUnit(id: Int?): DbUnit? {
        if ( id == null ) return null
        return data.units.find { it.id == id }
    }


    fun getUnitsNames(): Array<String> {
        return data.units.map { it.name_long }.toTypedArray()
    }
}