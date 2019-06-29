package com.example.ta.Adapter

import android.content.Context
import com.example.ta.Model.MCartTotal
import io.paperdb.Paper

class ShoppingCart {

    companion object {
        fun addItem(cartItem: MCartTotal) {
            val cart = ShoppingCart.getCart()

            val targetItem = cart.singleOrNull { it.mItems.id == cartItem.mItems.id }
            if (targetItem == null) {
                cartItem.quantity++
                cart.add(cartItem)
            } else {
                targetItem.quantity++
            }
            saveCart(cart)
        }

        fun removeItem(cartItem: MCartTotal, context: Context) {
            val cart = getCart()

            val targetItem = cart.singleOrNull { it.mItems.id == cartItem.mItems.id }
            if (targetItem != null) {
                if (targetItem.quantity > 0) {
                    targetItem.quantity--
                } else {
                    cart.remove(targetItem)
                }
            }

            saveCart(cart)
        }

        private fun saveCart(cart: MutableList<MCartTotal>) {
            Paper.book().write("cart", cart)
        }

        fun getCart(): MutableList<MCartTotal> {
            return Paper.book().read("cart", mutableListOf())
        }

        fun getShoppingCartSize(): Int {
            var cartSize = 0
            getCart().forEach {
                cartSize += it.quantity;
            }

            return cartSize
        }
    }

}