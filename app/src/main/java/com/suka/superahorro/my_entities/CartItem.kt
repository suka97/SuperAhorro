package com.suka.superahorro.my_entities

class CartItem (name : String) {

    fun getTotalPrice() : Float? {
        if ( unit_price == null || amount == null ) return null
        return unit_price!! * amount!!
    }

    var id: Long = 0

    var name: String = name

    var unit_price: Float? = null

    var amount: Float? = null

    var picture: String? = null


    var brand: String? = null

    var sku: String? = null

}