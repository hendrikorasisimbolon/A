package com.example.ta.Adapter

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ta.Model.MRiwayat
import com.example.ta.R
import kotlinx.android.synthetic.main.item_history.view.*
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RiwayatAdapter(var context: Context, var riwayat:ArrayList<MRiwayat>, var monlistener:OnNoteListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)
        return RiwayatHolder(layout)
    }

    override fun getItemCount(): Int = riwayat.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RiwayatHolder).bindItem(
            riwayat[position].id_Trans,
            riwayat[position].created,
            riwayat[position].kurir,
            riwayat[position].service,
            riwayat[position].status,
            riwayat[position].resi,
            riwayat[position].total,
            riwayat[position].ongkir,
            riwayat[position].diskon,
            this.monlistener
        )
    }
    class RiwayatHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        lateinit var monlistener: OnNoteListener
        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(id:String, cr:String, kr:String, srv:String, st:Int, rs:String, tl:Int, or:Int,ds:Int, monlistener: OnNoteListener)
        {
            this.monlistener = monlistener
            itemView.setOnClickListener(this)

            var locale = Locale("in", "ID")
            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            var stringA = cr.split(" ")
            var stringB = stringA[0].split("-")
            itemView.txt_tgl.text = stringB[2]
            itemView.txt_bln.text = stringB[1]
            itemView.txt_year.text = stringB[0]
            itemView.txt_idtrans.text = "Invoice number. " +id
            itemView.txt_kiriman.text = kr +" ("+srv+")"
            itemView.txt_resi.text = "Resi : "+ rs
            var dsc  = tl*(ds.toDouble() / 100)
            var diskon = (tl+or) - dsc

            if(dsc>0){
                itemView.txt_sblmd.text = formatRupiah.format(tl)
                itemView.txt_sblmd.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            else{
                itemView.txt_sblmd.visibility =View.INVISIBLE
            }


//            Log.e("kurir", kr.toString())
            itemView.txt_jumlah.text = formatRupiah.format(diskon)
            if(st == 1)
            {
                itemView.txt_status.text = "Not paid yet"
                itemView.txt_status.setBackgroundResource(R.drawable.round_step1)
                itemView.ly_stat.setBackgroundColor(Color.parseColor("#DCFF9900"))
            }
            else if(st == 2)
            {
                itemView.txt_status.text = "Paid"
                itemView.txt_status.setBackgroundResource(R.drawable.round_step2)
                itemView.ly_stat.setBackgroundColor(Color.parseColor("#BEEFFF36"))
            }
            else if (st == 3)
            {
                itemView.txt_status.text = "Sent"
                itemView.txt_status.setBackgroundResource(R.drawable.round_success)
                itemView.ly_stat.setBackgroundColor(Color.parseColor("#F52CFF01"))
            }
            else if(st == 4)
            {
                itemView.txt_status.text = "Arrived"
                itemView.txt_status.setBackgroundResource(R.drawable.round_arrived)
                itemView.txt_status.setTextColor(Color.WHITE)
                itemView.ly_stat.setBackgroundColor(Color.parseColor("#94008600"))
            }
            else{
                itemView.txt_status.text = "Cancel"
                itemView.txt_status.setBackgroundResource(R.drawable.round_notsucces)
                itemView.txt_status.setTextColor(Color.WHITE)
                itemView.ly_stat.setBackgroundColor(Color.parseColor("#BEFF0000"))
            }
        }
        override fun onClick(v: View?) {
            monlistener.onNoteClick(adapterPosition)
        }
    }
    interface OnNoteListener {
        fun onNoteClick(position: Int)
    }
}


