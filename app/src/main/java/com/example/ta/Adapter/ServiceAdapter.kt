package com.example.ta.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.ta.Model.MService
import com.example.ta.R
import kotlinx.android.synthetic.main.activity_checkout.view.*
import kotlinx.android.synthetic.main.item_ekpedisi.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ServiceAdapter(var context: Context, var list:ArrayList<MService>, var monlistener:OnNoteListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View = LayoutInflater.from(context).inflate(R.layout.item_ekpedisi,parent,false)
        return ServiceHolder(v)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ServiceHolder).bind(
            list[position].kode,
            list[position].service,
            list[position].tarif,
            list[position].estimasi,
            this.monlistener
        )
    }

    class ServiceHolder(itemView:View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        lateinit var monlistener: OnNoteListener
        @SuppressLint("SetTextI18n")
        fun bind(kd:String, srv:String, trf:Int, est:String, monlistener:OnNoteListener)
        {
            this.monlistener = monlistener
            var locale = Locale("in", "ID")
            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            itemView.txt_ongkir.text = formatRupiah.format(trf)
            itemView.txt_code.text = kd + " " + srv
            itemView.txt_estimasi.text = est

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

