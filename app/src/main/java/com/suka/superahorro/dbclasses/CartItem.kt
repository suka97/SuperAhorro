package com.suka.superahorro.dbclasses

import android.os.Parcelable
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbCartItem
import kotlinx.parcelize.Parcelize

@Parcelize
class CartItem (var data: DbCartItem, var cartPos: Int) : Parcelable {
    constructor(name : String) : this(DbCartItem(name = name), 0)

    fun getTotalPrice() : Float? {
        if ( data.unit_price == null || data.amount == null ) return null
        return data.unit_price!! * data.amount!!
    }


    fun linkModel(model: Model) {
        data.model = DbCartItem.Model(
            id = model.data.id,
            name = model.data.name,
            sku = model.data.sku
        )
    }
}