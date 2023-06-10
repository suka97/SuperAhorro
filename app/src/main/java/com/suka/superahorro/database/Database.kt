package com.suka.superahorro.database

import com.suka.superahorro.my_entities.CartItem

class Database {
    companion object {
        fun getCartItems(): MutableList<CartItem> {
            return mutableListOf<CartItem>()
        }

        fun insertCartItem(cartItem: CartItem) {

        }

        fun removeCartItem(cartItem: CartItem) {

        }

    }
}