package com.example.ta.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.*

import com.example.ta.OrderAct
import com.example.ta.ProductAllAct
import com.example.ta.R
import com.squareup.picasso.Picasso
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_product_all.*
import kotlinx.android.synthetic.main.product_row_item.view.*
import java.security.AccessController.getContext
import java.text.NumberFormat
import java.util.*

class ProductAdp(var context: Context, var products: List<MItems> = arrayListOf()) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ProductAdp.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProductAdp.ViewHolder {
        // The layout design used for each list item
        val view = LayoutInflater.from(context).inflate(R.layout.product_row_item, p0,false)
        //Paper.init(context);
        return ViewHolder(view)

    }

    // This returns the size of the list.
    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(viewHolder: ProductAdp.ViewHolder, position: Int) {
        //we simply call the `bindProduct` function here
        viewHolder.bindProduct(products[position])


        (context as ProductAllAct).coordinator
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        // This displays the product information for each item
        @SuppressLint("CheckResult")
        fun bindProduct(product: MItems) {
            var locale = Locale("in", "ID")
            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            itemView.product_name.text = product.judul_produk
            itemView.product_price.text = formatRupiah.format(product.harga_normal)
            Picasso.with(itemView.context)
                .load("http://192.168.43.180/ecommerce/assets/images/produk/" + product.foto + product.foto_type)
                .into(itemView.product_image)


//            Picasso.get().load(product.photos[0].filename).fit().into(itemView.product_image)


//            io.reactivex.Observable.create(ObservableOnSubscribe<MutableList<MCartTotal>> { // Gantiiiiiiiiii
//
//                itemView.addToCart.setOnClickListener { view ->
//
//                    var url = "http://192.168.43.180/udemy/insert_cart.php?user_id="+ MCart.user_id +"&id_produk="+ MCart.itemId +
//                            "&total_qty=1"
//                    var rq: RequestQueue = Volley.newRequestQueue(itemView.context)
//                    var sr = StringRequest(Request.Method.GET,url, Response.Listener { response ->
//
//                    }, Response.ErrorListener { error ->
////                        Toast.makeText(activity,error.message,Toast.LENGTH_LONG).show()
//                        Log.e("ERROR NYIMPAN CART", error.message)
//                    })
//                    rq.add(sr)
//
////                    val item = MCartTotal(product)
////
////                    ShoppingCart.addItem(item)
////                    //notify users
//                    Snackbar.make(
//                        (itemView.context as ProductAllAct).coordinator,
//                        "${product.judul_produk} added to your cart",
//                        Snackbar.LENGTH_LONG
//                    ).show()
//
//                    it.onNext(ShoppingCart.getCart(itemView.context))
//
//                }
//
//            }).subscribe { cart ->
//
//                var quantity = total_cart
//
////                cart.forEach { cartItem ->
////
////                    quantity += cartItem.quantity
////                }
//
//                (itemView.context as ProductAllAct).cart_size.text = quantity.toString()
//                Toast.makeText(itemView.context, "Cart size $quantity", Toast.LENGTH_SHORT).show()
//            }

        }

    }
}