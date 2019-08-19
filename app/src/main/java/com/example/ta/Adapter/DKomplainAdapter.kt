package com.example.ta.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta.BuktiKomplainAct
import com.example.ta.Model.MDaftarKomplain
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.R
import kotlinx.android.synthetic.main.item_komplain.view.*

class DKomplainAdapter (var context: Context, var mDaftar:ArrayList<MDaftarKomplain>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.item_komplain,parent,false)
        return KomplainHolder(v)
    }

    override fun getItemCount(): Int = mDaftar.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as KomplainHolder).bindItem(
            mDaftar[position].id_komplain,
            mDaftar[position].status,
            mDaftar[position].selesai,
            mDaftar[position].foto,
            mDaftar[position].foto_type
        )
    }
    class KomplainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(idK:String, st:String, sl:String, ft:String, ftt:String) {
            if (st == "1" && sl == "0"){
                itemView.btn_bukti.visibility = View.GONE
                itemView.txt_carapengembalian.text = "Retur Barang"
                itemView.txt_status_komplain.text = "Sedang diproses"
                itemView.txt_status_komplain.setTextColor(Color.BLUE)
            }
            else if( st == "0" && sl=="0"){
                itemView.btn_bukti.visibility = View.GONE
                itemView.txt_carapengembalian.text = "Retur Dana"
                itemView.txt_status_komplain.text = "Sedang diproses"
                itemView.txt_status_komplain.setTextColor(Color.BLUE)
            }
            else if (st == "1" && sl == "1"){
                itemView.btn_bukti.visibility = View.GONE
                itemView.txt_carapengembalian.text = "Retur Barang"
                itemView.txt_status_komplain.text = "Selesai"
                itemView.txt_status_komplain.setTextColor(Color.GREEN)
            }
            else if (st == "0" && sl == "1"){
                itemView.txt_carapengembalian.text = "Retur Dana"
                itemView.txt_status_komplain.text = "Selesai"
                itemView.txt_status_komplain.setTextColor(Color.GREEN)
                itemView.btn_bukti.setOnClickListener {
                    var intent = Intent(itemView.context, BuktiKomplainAct::class.java)
                    intent.putExtra("idK",idK)
                    itemView.context.startActivity(intent)
                }
            }
            else {
                itemView.txt_carapengembalian.text = "Gagal"
                itemView.txt_status_komplain.text = "Gagal"
                itemView.txt_status_komplain.setTextColor(Color.RED)
                itemView.btn_bukti.setOnClickListener {
                    var intent = Intent(itemView.context, BuktiKomplainAct::class.java)
                    intent.putExtra("idK",idK)
                    itemView.context.startActivity(intent)
                }
            }
            Glide.with(itemView.context).load(url_website+"/ecommerce/assets/images/komplain/"+ft+ftt)
                .into(itemView.foto_komplain)
            itemView.txt_kd_komplian.text = idK
        }
    }

}


