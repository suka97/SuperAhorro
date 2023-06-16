package com.suka.superahorro.database

data class User(
    val id: String,
    val name: String,
    val item: Item
)


data class Item(
    val id: String,
    val name: String
)


data class Model(
    val id: String,
    val name: String,
    val item_id: String,
    val unit: String,
    val base_unit: String,
    //val last_price: Float? = null,
    val img: String? = null
)


data class Cart(
    val id: String,
    val shop: String,
    val items: List<Item>,
    val disc: Discount,
    val total: Float
) {
    data class Discount(
        val type: String,
        val value: Float
    )
}