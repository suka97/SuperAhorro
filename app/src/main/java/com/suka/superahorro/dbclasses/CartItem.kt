package com.suka.superahorro.dbclasses

import android.os.Parcelable
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbCartItem
import com.suka.superahorro.database.DbItemRef
import com.suka.superahorro.database.DbModelRef
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
        data.model = model.toCartRef()
        val lastBuy = model.getLastBuy()
        if ( lastBuy != null ) {
            data.unit_price = lastBuy.price
            data.amount = lastBuy.amount
        }
    }


    fun toItemRef(): DbItemRef {
        return DbItemRef(
            id = data.id,
            name = data.name
        )
    }


    fun toModelRef(): DbModelRef? {
        if (data.model == null) return null
        return DbModelRef(
            id = data.model!!.id,
            name = data.model!!.name,
            sku = data.model!!.sku
        )
    }
}