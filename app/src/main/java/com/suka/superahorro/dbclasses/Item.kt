package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbItemRef

class Item(var data: DbItemRef) {
    constructor(name: String) : this(DbItemRef(name = name))
    constructor(id: Int, name: String) : this(DbItemRef(id = id, name = name))
}