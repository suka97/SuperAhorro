package com.suka.superahorro.dbclasses

import android.os.Parcelable
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbCartItem
import com.suka.superahorro.database.DbItemRef
import kotlinx.parcelize.Parcelize

@Parcelize
class CartItem (var data: DbCartItem, var cartPos: Int) : Parcelable {
    constructor(name : String) : this(DbCartItem(name = name), 0)
    constructor(item: DbItemRef) : this(DbCartItem(
        id = item.id,
        name = item.name,
    ), 0)

    fun getTotalPrice() : Float? {
        if ( data.unit_price == null || data.amount == null ) return null
        return data.unit_price!! * data.amount!!
    }


    fun linkModel(model: Model) {
        data.model = DbCartItem.Model(
            id = model.data.id,
            sku = model.data.sku,
            name = model.data.name,
            unit = model.data.unit,
            base_unit = model.data.base_unit,
            img = model.data.img
        )
        data.unit_price = model.data.last_price
    }
}