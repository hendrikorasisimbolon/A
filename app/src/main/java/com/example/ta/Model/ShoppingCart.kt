package com.example.ta.Model

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.MTotalCart.Companion.total_cart

import io.paperdb.Paper

class ShoppingCart {


    companion object {
        fun addItem(cartItem: MCartTotal, context: Context) {
            val cart = getCart(context)

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
            val cart = getCart(context)

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

        fun getCart(context: Context): MutableList<MCartTotal> {

            var url = "http://192.168.43.180/udemy/get_cart.php?user_id="+MCart.user_id
            var rq: RequestQueue = Volley.newRequestQueue(context)

            var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->

                //            UserInfo.jumlahCart = response.length()
                for (x in 0..response.length() - 1) {
//                    list.mItems = MItems()
                    Log.e(
                        "isi : ",
                        "Produk : " + response.getJSONObject(x).getString("judul_produk") + "\n" + "Harga Rp. :" + response.getJSONObject(
                            x
                        ).getString("harga_diskon") + "\n" +
                                "Total qty : " + response.getJSONObject(x).getString("total_qty")
                    )
                }

            }, Response.ErrorListener { error ->
                Toast.makeText(context,error.message, Toast.LENGTH_LONG).show()
            })
            rq.add(jar)
            return Paper.book().read("cart", mutableListOf())
        }

        fun getShoppingCartSize(): Int {
            var cartSize = total_cart.toInt()
//            getCart().forEach {
//                cartSize += it.quantity;
//            }

            return cartSize
        }
    }

}