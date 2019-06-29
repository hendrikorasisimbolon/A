package com.example.ta.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ta.Model.MCartTotal
import com.example.ta.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_list_item.view.*
import java.text.NumberFormat
import java.util.*

class ShoppingCartAdapter(var context: Context, var cartItems: List<MCartTotal>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ShoppingCartAdapter.ViewHolder {

        // The layout design used for each list item
        val layout = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false)

        return ViewHolder(layout)
    }

    // This returns the size of the list.
    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(viewHolder: ShoppingCartAdapter.ViewHolder, position: Int) {

        //we simply call the `bindItem` function here
        viewHolder.bindItem(cartItems[position])
    }
    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun bindItem(cartItem: MCartTotal) {
            var locale = Locale("in", "ID")
            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            // This displays the cart item information for each item
            Picasso.with(itemView.context)
                .load("http://192.168.43.180/ecommerce/assets/images/produk/" + cartItem.mItems.foto + cartItem.mItems.foto_type)
                .into(itemView.product_image)
           // Picasso.get().load(cartItem.product.photos[0].filename).fit().into(itemView.product_image)
            itemView.product_name.text = cartItem.mItems.judul_produk
            itemView.product_price.text = formatRupiah.format(cartItem.mItems.harga_normal)
//            itemView.product_quantity.text = cartItem.quantity.toString()
//            itemView.product_quantity.value = cartItem.quantity

        }
    }

}