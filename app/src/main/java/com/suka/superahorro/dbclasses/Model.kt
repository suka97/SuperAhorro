package com.suka.superahorro.dbclasses

import com.google.firebase.Timestamp
import com.suka.superahorro.database.DbCartItem
import com.suka.superahorro.database.DbModel
import com.suka.superahorro.database.DbModelRef

class Model(var data: DbModel) {
    constructor(name: String) : this(DbModel(name=name))

    data class LastBuy(
        var date: Timestamp,
        var price: Float,
        var amount: Float,
        var cart: String
    )


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
            img = data.img,
            brand = data.brand,
            sale_mode = data.sale_mode,
            base_unit = data.base_unit,
            content = data.content,
            note = data.note,
        )
    }


    fun getLastBuy(): LastBuy? {
        if ( data.hist_dates.isEmpty() ) return null
        return LastBuy(
            date = data.hist_dates.last(),
            price = data.hist_prices.last(),
            amount = data.hist_amounts.last(),
            cart = data.hist_carts.last()
        )
    }
}