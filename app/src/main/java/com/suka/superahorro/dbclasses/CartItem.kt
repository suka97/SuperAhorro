package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbCartItem

class CartItem (var data: DbCartItem) {
    fun getTotalPrice() : Float? {
        if ( data.unit_price == null || data.amount == null ) return null
        return data.unit_price!! * data.amount!!
    }
}