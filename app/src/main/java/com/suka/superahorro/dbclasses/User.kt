package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbUnit
import com.suka.superahorro.database.DbUser
import kotlin.random.Random

class User (var data: DbUser) {
    constructor(id: String) : this(DbUser(id=id))

    fun addNewItem(name: String): Item {
        var id = Random.nextLong(from = 10000, until = 99999)
        while (data.items.any { it.id == id }) {
            id = Random.nextLong(from = 10000, until = 99999)
        }
        val newItem = Item(id=id, name=name)
        data.items.add(newItem.toRef())
        return newItem
    }


    fun addNewUnit(nameLong: String): DbUnit {
        var id = Random.nextLong(from = 100, until = 200)
        while (data.units.any { it.id == id }) {
            id = Random.nextLong(from = 100, until = 200)
        }
        val newUnit = DbUnit(id=id, name_long=nameLong)
        data.units.add(newUnit)
        return newUnit
    }


    fun getUnit(id: Long?): DbUnit? {
        if ( id == null ) return null
        return data.units.find { it.id == id }
    }
}