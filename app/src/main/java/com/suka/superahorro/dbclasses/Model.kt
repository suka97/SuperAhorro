package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbCartItem
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


    fun toCartRef(): DbCartItem.Model {
        return DbCartItem.Model(
            id = data.id,
            name = data.name,
            sku = data.sku,
            unit = data.unit,
            base_unit = data.base_unit,
            img = data.img
        )
    }
}