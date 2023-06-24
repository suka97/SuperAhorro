package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbItem

class Item(var data: DbItem) {
    constructor(name: String) : this(DbItem(name = name))
    constructor(id: Int, name: String) : this(DbItem(id = id, name = name))
}