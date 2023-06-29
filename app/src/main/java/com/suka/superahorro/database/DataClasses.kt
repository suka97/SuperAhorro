package com.suka.superahorro.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize



data class DbUser(
    var id: String = "",
    var name: String = "",
    var items: MutableList<DbItemRef> = mutableListOf(),
)


data class DbItemRef(
    var id: Int = 0,
    var name: String = ""
)


data class DbItem(
    var id: Int = 0,
    var name: String = "",
    var models: MutableList<DbModelRef> = mutableListOf(),
)


data class DbModelRef(
    var id: String = "",
    var name: String = "",
    var sku: String? = null,
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


@Parcelize
data class DbCart(
    var id: String = "",
    var shop: String? = null,
    var status: String = STATUS_OPENED,
    var items: MutableList<DbCartItem> = mutableListOf(),
    var disc: Discount? = null,
    var total: Float = 0f
): Parcelable {
    @Parcelize
    data class Discount(
        var type: String = "",
        var value: Float = 0f
    ): Parcelable

    companion object {
        const val STATUS_OPENED = "opened"
        const val STATUS_CLOSED = "closed"
    }
}


@Parcelize
data class DbCartItem(
    var id: Int = 0,
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