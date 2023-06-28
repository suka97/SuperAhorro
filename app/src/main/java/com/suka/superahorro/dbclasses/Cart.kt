package com.suka.superahorro.dbclasses

import android.os.Parcelable
import com.suka.superahorro.database.DbCart
import com.suka.superahorro.packages.round
import kotlinx.parcelize.Parcelize

@Parcelize
class Cart(var data: DbCart): Parcelable {
    constructor(shop: String): this(DbCart(shop=shop))

    fun size(): Int {
        return data.items.size
    }

    fun getLastItem(): CartItem {
        return CartItem(data.items.last(), data.items.size - 1)
    }

    fun getItem(index: Int): CartItem {
        return CartItem(data.items[index], index)
    }

    fun deleteItem(index: Int) {
        data.items.removeAt(index)
        updateTotal()
    }

    fun insertItem(item: CartItem) {
        data.items.add(item.data)
        updateTotal()
    }

    fun setItem(CartItem: CartItem) {
        data.items[CartItem.cartPos] = CartItem.data
        updateTotal()
    }


    private fun updateTotal() {
        var total = 0f
        data.items.forEach {
            if (it.unit_price != null && it.amount != null)
                total += it.unit_price!! * it.amount!!
        }
        data.total = total.round(2)
    }
}