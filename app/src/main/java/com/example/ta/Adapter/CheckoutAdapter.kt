package com.example.ta.Adapter

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ta.Fragment.DltFragment
import com.example.ta.Fragment.F5Fragment
import com.example.ta.Model.MCart
import com.example.ta.Model.MItemDetail
import com.example.ta.Model.MKeranjang
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.R
import kotlinx.android.synthetic.main.itemuntukcheckout.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class CheckoutAdapter (var context: Context, var cartItems:ArrayList<MKeranjang>)
    : RecyclerView.Adapter<ViewHolder>()
{
    companion object{
        var catat:ArrayList<MKeranjang> = ArrayList()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.itemuntukcheckout, parent, false)
        return CheckoutHolder(layout)
    }

    override fun getItemCount(): Int = cartItems.size

    @TargetApi(Build.VERSION_CODES.P)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CheckoutHolder).bindItem(
            cartItems[position].idP,
            cartItems[position].judul,
            cartItems[position].harga,
            cartItems[position].foto,
            cartItems[position].foto_type,
            cartItems[position].qty,
            cartItems[position].stok
            )
    }

    class CheckoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        @TargetApi(Build.VERSION_CODES.P)
        @RequiresApi(Build.VERSION_CODES.P)
        @SuppressLint("SetTextI18n", "NewApi")
        fun bindItem(idP:String, judul:String, harga:Int, foto:String, foto_type:String, qty:Int, stok:Int)
        {
//            var listner:CharCountTextView.CharCountChangedListener ?= null
            var locale = Locale("in", "ID")
            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            if(foto!=""&&foto_type!="" && foto!=null && foto_type!=null) {
                Glide.with(itemView.context)
                    .load(url_website + "/ecommerce/assets/images/produk/" + foto + foto_type)
                    .into(itemView.foto_cart)
            }
            itemView.judul_produkcart.text = judul
            itemView.txt_harga_cart.text = formatRupiah.format(harga)
            itemView.product_quantity.text = qty.toString()
//            itemView.hitung.setEditText(itemView.catatan)
//            itemView.hitung.setCharCountChangedListener { countRemaining, hasExceededLimit ->
//                if(hasExceededLimit){
//                    itemView.catatan.selectAll()
//                    itemView.catatan.setBackgroundColor(Color.RED)
//                }
//            }


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
                        itemView.berat_procuctcart.text = "Berat : " + MItemDetail.data[i].berat + " gram / satuan"
                    }
                }

                var obj = F5Fragment() // fragment
                var mana = (itemView.context as AppCompatActivity).fragmentManager
                obj.show(mana,"Rfs")
            }

            itemView.btn_qty_remove.setOnClickListener {
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

            itemView.btn_deletecart.setOnClickListener{
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
            catat.add(MKeranjang(idP,judul,harga,qty,foto,foto_type,itemView.catatan.text.toString(),stok))
            Log.e("CATATAN",itemView.catatan.text.toString())
        }
    }

}