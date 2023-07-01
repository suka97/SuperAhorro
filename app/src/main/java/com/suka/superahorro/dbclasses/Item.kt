package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbItem
import com.suka.superahorro.database.DbItemRef

class Item(var data: DbItem) {
    constructor(name: String) : this(DbItem(name = name))
    constructor(id: Int, name: String) : this(DbItem(id = id, name = name))

    fun toRef(): DbItemRef {
        return DbItemRef(id = data.id, name = data.name)
    }


    fun getModelOptionsName(): MutableList<String> {
        val options = mutableListOf<String>()
        for (model in data.models) {
            options.add(model.name)
        }
        return options
    }
}