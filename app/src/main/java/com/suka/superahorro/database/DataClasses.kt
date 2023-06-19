package com.suka.superahorro.database

data class DbUser(
    var id: String = "",
    var name: String = "",
)


data class DbItem(
    var id: String,
    var name: String
)


data class DbModel(
    var id: String = "",
    var name: String = "",
    var item_id: String? = null,
    var unit: String? = null,
    var base_unit: String? = null,
    //var last_price: Float? = null,
    var img: String? = null
)


data class DbCart(
    var id: String = "",
    var shop: String? = null,
    var items: MutableList<DbCartItem> = mutableListOf(),
    var disc: Discount? = null,
    var total: Float = 0f
) {
    data class Discount(
        var type: String = "",
        var value: Float = 0f
    )
}


data class DbCartItem(
    var id: String = "",
    var name: String = "",
    var model: Model? = null,
    var amount: Float? = null,
    var unit_price: Float? = null,
) {
    data class Model(
        var id: String = "",
        var name: String = "",
        var unit: String? = null,
        var base_unit: String? = null,
        var img: String? = null
    )
}