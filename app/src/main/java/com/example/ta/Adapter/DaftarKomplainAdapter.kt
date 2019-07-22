package com.example.ta.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ta.DaftarKomplainAct
import com.example.ta.R
import kotlinx.android.synthetic.main.ly_item_kom.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DaftarKomplainAdapter(var context: Context, var promo: ArrayList<DaftarKomplainAct.Terbeli>, var monlistener:OnNoteListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.ly_item_kom, parent, false)
        return DfHolder(v)
    }

    override fun getItemCount(): Int = promo.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DfHolder).bindItem(
            promo[position].judul_produk,
            promo[position].created,
            promo[position].id_transdet,
            promo[position].produk_id,
            this.monlistener
        )
    }

    class DfHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var locale = Locale("in", "ID")
        lateinit var monlistener: OnNoteListener
        var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
        fun bindItem(jd:String,cr:String,tr:String,id:String,monlistener: OnNoteListener) {
            this.monlistener = monlistener
            itemView.txt_judul.text = jd
            itemView.tggl.text = cr
            itemView.setOnClickListener(this)

        }
        override fun onClick(v: View?) {
            monlistener.onNoteClick(adapterPosition)

        }
    }
    interface OnNoteListener {
        fun onNoteClick(position: Int)
    }
}
