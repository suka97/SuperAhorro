package com.suka.superahorro.database

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize



data class DbUser(
    var id: String = "",
    var name: String = "",
    var items: MutableList<DbItemRef> = mutableListOf(),
    var units: MutableList<DbUnit> = mutableListOf(),
)


data class DbUnit(
    var id: Int = 0,
    var name_short: String = "",
    var name_long: String = "",
    var sell_unit: String = "",
    var sell_amount: Float = 0f,
)


data class DbItemRef(
    var id: Int = 0,
    var name: String = ""
) {
    override fun toString(): String {
        return name
    }
}


@Parcelize
data class DbItem(
    var id: Int = 0,
    var name: String = "",
    var models: MutableList<DbModelRef> = mutableListOf(),
): Parcelable


@Parcelize
data class DbModelRef(
    var id: String = "",
    var name: String = "",
    var sku: String? = null,
): Parcelable {
    override fun toString(): String {
        return name
    }
}


data class DbModel(
    var id: String = "",
    var sku: String? = null,
    var name: String = "",
    var item: DbItemRef = DbItemRef(),
    var img: String? = null,
    var last_buy: LastBuy? = null,
    var brand: String? = null,
    var sale_mode: String? = null,  // "Lata 100g, 1kg, 5kg"
    var base_unit: String? = null,
    var content: Float? = null,
    var note: String? = null,
) {
    data class LastBuy(
        var date: Timestamp = Timestamp.now(),
        var price: Float = 0f,
        var amount: Float = 0f
    )
}


@Parcelize
data class DbCart(
    var id: String = "",
    var shop: String = "",
    var last_edit: Timestamp = Timestamp.now(),
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