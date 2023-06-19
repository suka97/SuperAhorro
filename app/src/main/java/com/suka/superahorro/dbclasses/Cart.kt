package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbCart

class Cart(var data: DbCart) {

    fun size(): Int {
        return data.items.size
    }

    fun getItem(index: Int): CartItem {
        return CartItem(data.items[index], index)
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
            total += it.unit_price!! * it.amount!!
        }
        data.total = total
    }
}