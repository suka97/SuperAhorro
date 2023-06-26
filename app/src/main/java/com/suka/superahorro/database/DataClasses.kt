package com.suka.superahorro.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DbUser(
    var id: String = "",
    var name: String = "",
    var items: MutableList<DbItem> = mutableListOf(),
)


data class DbItem(
    var id: Int = 0,
    var name: String = ""
)


data class DbModel(
    var id: String = "",
    var sku: String? = null,
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


@Parcelize
data class DbCartItem(
    var id: String = "",
    var name: String = "",
    var model: Model? = null,
    var amount: Float? = null,
    var unit_price: Float? = null,
) : Parcelable {
    @Parcelize
    data class Model(
        var id: String = "",
        var sku: String? = null,
        var name: String = "",
        var unit: String? = null,
        var base_unit: String? = null,
        var img: String? = null
    ) : Parcelable
}