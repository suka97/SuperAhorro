package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbModel
import com.suka.superahorro.database.DbModelRef

class Model(var data: DbModel) {
    constructor(name: String) : this(DbModel(name=name))

    fun toRef(): DbModelRef {
        return DbModelRef(
            id = data.id,
            name = data.name,
            sku = data.sku
        )
    }
}