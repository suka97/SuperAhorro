package com.suka.superahorro.dbclasses

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.suka.superahorro.database.Database
import com.suka.superahorro.database.DbCart
import com.suka.superahorro.database.DbModel
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


    suspend fun close() {
        data.last_edit = Timestamp.now()

        // creo un nuevo cart con los mismos datos pero cerrado con los items completados
        val newCart = this
        newCart.data.items = mutableListOf()
        newCart.data.status = DbCart.STATUS_CLOSED
        Database.addCart(newCart)

        for ( i in data.items.indices ) {
            val item = data.items[i]
            if ( item.unit_price==null || item.amount==null ) continue
            if ( item.model == null ) continue

            newCart.insertItem( this.getItem(i) )
            this.deleteItem(i)

            Database.setModelLastBuy(item.model!!.id, Model.LastBuy(
                date = newCart.data.last_edit,
                price = item.unit_price!!,
                amount = item.amount!!,
                cart = newCart.data.id
            ))
        }

        Database.saveCart(this)
        Database.saveCart(newCart)
    }
}