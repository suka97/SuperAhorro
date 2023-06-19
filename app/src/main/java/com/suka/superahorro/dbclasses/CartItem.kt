package com.suka.superahorro.dbclasses

import android.os.Parcelable
import com.suka.superahorro.database.DbCartItem
import kotlinx.parcelize.Parcelize

@Parcelize
class CartItem (var data: DbCartItem, var cartPos: Int) : Parcelable {
    fun getTotalPrice() : Float? {
        if ( data.unit_price == null || data.amount == null ) return null
        return data.unit_price!! * data.amount!!
    }

    constructor(name : String) : this(DbCartItem(name = name), 0)
}