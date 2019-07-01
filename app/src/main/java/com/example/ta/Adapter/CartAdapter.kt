package com.example.ta.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Fragment.DltFragment
import com.example.ta.Fragment.F5Fragment
import com.example.ta.Model.MCart
import com.example.ta.Model.MItemDetail
import com.example.ta.Model.MKeranjang
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_list_item.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class CartAdapter (var context: Context, var cartItems: ArrayList<MKeranjang>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false)

        return ViewHolder(layout)
}

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindItem(
            cartItems[position].idP,
            cartItems[position].judul,
            cartItems[position].harga,
            cartItems[position].foto,
            cartItems[position].foto_type,
            cartItems[position].qty)
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindItem(idP:String, judul:String,harga:Int, foto:String, foto_type:String, qty:Int)
        {
            var locale = Locale("in", "ID")
            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            // This displays the cart item information for each item
            Picasso.with(itemView.context)
                .load(url_website+"/ecommerce/assets/images/produk/" + foto + foto_type)
                .into(itemView.product_image)
            // Picasso.get().load(cartItem.product.photos[0].filename).fit().into(itemView.product_image)
            itemView.product_name.text = judul
            itemView.product_price.text = formatRupiah.format(harga)
            itemView.product_quantity.text = qty.toString()

            itemView.btn_qty_add.setOnClickListener{
                var t:Int = itemView.product_quantity.text.toString().toInt()
                t++
                itemView.product_quantity.text = t.toString()
                var url = url_website+"/udemy/insert_cart.php?user_id="+ MCart.user_id +"&id_produk="+ idP +
                        "&total_qty=1"
                var rq: RequestQueue = Volley.newRequestQueue(itemView.context)
                var sr = StringRequest(Request.Method.GET,url, Response.Listener { response ->

                }, Response.ErrorListener { error ->
                    Toast.makeText(itemView.context, error.message, Toast.LENGTH_LONG).show()
                })
                rq.add(sr)
                MTotalCart.total_harga += harga.toString().toInt()

                for( i in 0..MItemDetail.data.size-1)
                {
                    if (MItemDetail.data[i].id == idP.toInt())
                    {
                        MTotalCart.total_berat += MItemDetail.data[i].berat.toInt()
                    }
                }

                var obj = F5Fragment() // fragment
                var mana = (itemView.context as AppCompatActivity).fragmentManager
                obj.show(mana,"Rfs")
            }

            itemView.btn_qty_remove.setOnClickListener{
                var t:Int = itemView.product_quantity.text.toString().toInt()
                t--
                itemView.product_quantity.text = t.toString()
                if (t > 0)
                {
                    var url = url_website+"/udemy/insert_cart.php?user_id="+ MCart.user_id +"&id_produk="+ idP +
                            "&total_qty=-1"
                    var rq: RequestQueue = Volley.newRequestQueue(itemView.context)
                    var sr = StringRequest(Request.Method.GET,url, Response.Listener { response ->

                    }, Response.ErrorListener { error ->
                        Toast.makeText(itemView.context, error.message, Toast.LENGTH_LONG).show()
                    })

                    rq.add(sr)
                    MTotalCart.total_harga -= harga.toString().toInt()
                    var obj = F5Fragment() // fragment
                    var mana = (itemView.context as AppCompatActivity).fragmentManager
                    obj.show(mana,"Rfs")
                }
                else
                {
                    var url = url_website+"/udemy/insert_cart.php?user_id="+ MCart.user_id +"&id_produk="+ idP +
                            "&total_qty=-1"
                    var rq: RequestQueue = Volley.newRequestQueue(itemView.context)
                    var sr = StringRequest(Request.Method.GET,url, Response.Listener { response ->

                    }, Response.ErrorListener { error ->
                        Toast.makeText(itemView.context, error.message, Toast.LENGTH_LONG).show()
                    })

                    rq.add(sr)
                    MTotalCart.total_harga -= harga.toString().toInt()
                    for( i in 0..MItemDetail.data.size-1)
                    {
                        if (MItemDetail.data[i].id == idP.toInt())
                        {
                            MTotalCart.total_berat -= MItemDetail.data[i].berat.toInt()
                        }
                    }
                    MCart.itemId=idP.toInt()
                    var obj = DltFragment() // fragment
                    var mana = (itemView.context as AppCompatActivity).fragmentManager
                    obj.show(mana,"Dlt")
                }

            }
        }
    }

}