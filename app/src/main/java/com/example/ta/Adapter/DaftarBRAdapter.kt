package com.example.ta.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ta.Model.MDaftarBR
import com.example.ta.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_history_detail.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DaftarBRAdapter (var context: Context, var daftar:ArrayList<MDaftarBR>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_history_detail, parent, false)
        return DaftarHolder(layout)
    }

    override fun getItemCount(): Int = daftar.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DaftarHolder).bindItem(
            daftar[position].catatan,
            daftar[position].foto,
            daftar[position].foto_type,
            daftar[position].harga,
            daftar[position].judul_produk,
            daftar[position].subtotal,
            daftar[position].total_qty
        )
    }
    class DaftarHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(ctt:String, ft:String, ftt:String, hrg:Int, jdl:String, sbttl:Int, ttlqty:Int)
        {
            var locale = Locale("in", "ID")
            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            itemView.txt_jdlbrg.text = jdl
            itemView.txt_totall.text = "Harga : "+ formatRupiah.format(sbttl)
            itemView.txt_qtyy.text = "Qty      : "+ ttlqty.toString()
            itemView.txt_cttn.text = ctt
            Log.e("catatan",ctt)
            Picasso.with(itemView.context).load("http://192.168.43.180/ecommerce/assets/images/produk/" + ft + ftt)
                .into(itemView.img_ft)
        }
    }

}

