package com.suka.superahorro.database

import com.suka.superahorro.my_entities.CartItem
import com.suka.superahorro.my_entities.User

class Database {
    companion object {
        // cartItems
        fun getCartItems(): MutableList<CartItem> {
            return mutableListOf<CartItem>()
        }
        fun insertCartItem(cartItem: CartItem) {

        }
        fun removeCartItem(cartItem: CartItem) {

        }


        // user
        fun getUser(): User {
            return User("","")
        }
    }
}