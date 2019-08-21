package com.example.ta.Adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta.Fragment.QtyFragment
import com.example.ta.Model.MCart
import com.example.ta.Model.MItemDetail
import com.example.ta.Model.Url_Volley
import com.haozhang.lib.SlantedTextView
import kotlinx.android.synthetic.main.product_row_item.view.*
import java.text.NumberFormat
import java.util.*


@Suppress("DEPRECATION")
public class ItemAdapter (var context:Context, var list:ArrayList<MItemDetail>, var monlistener:OnNoteListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val PAGE_SIZE = 10

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        var v: View = LayoutInflater.from(context).inflate(com.example.ta.R.layout.product_row_item, p0, false)
        return ItemHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        (p0 as ItemHolder).bind(
            list[p1]!!.id,
            list[p1]!!.judul_produk,
            list[p1]!!.harga_diskon,
//            list[p1]!!.deksripsi,
            list[p1]!!.foto,
            list[p1]!!.foto_type,
            list[p1]!!.diskon,
            list[p1]!!.harga_normal,
            list[p1]!!.stok,
            this.monlistener
        )

//        (context as ItemsAct).coordinator
//        (context as ItemAllAct).coordinator

//        (p0 as ItemHolder).bind(MItems(list[p1].id,list[p1].judul_produk,list[p1].harga_normal,list[p1].deksripsi,list[p1].foto,list[p1].foto_type),clickListener)

    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        lateinit var monlistener: OnNoteListener

//        fun bind(id: Int, j: String, h: Double, dk: String, f: String, ft: String,ds:Double, hn:Double, st:Double, monlistener: OnNoteListener)
        fun bind(id: Int, j: String, h: Double, f: String, ft: String,ds:Double, hn:Double, st:Double, monlistener: OnNoteListener)
        {
            this.monlistener = monlistener
            var locale = Locale("in", "ID")
            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            val nf = NumberFormat.getNumberInstance()
            nf.maximumFractionDigits = 0

            if(st == 0.0){
                itemView.stok.text = "Stok Habis"
                itemView.stok.setTextColor(Color.RED)
            }

            itemView.product_name.text = j
//            itemView.item_deskripsi.text = dk
//            itemView.product_price.text = formatRupiah.format(h)

            if(ds>0){
                itemView.product_price_diskon.text = formatRupiah.format(hn)
                itemView.product_price_diskon.paintFlags = STRIKE_THRU_TEXT_FLAG
                itemView.product_price.text = formatRupiah.format(h)
                itemView.stvDiscount.setText("Disc."+nf.format(ds)+" %")
                    .setTextColor(Color.WHITE)
                    .setSlantedLength(70)
                    .setMode(SlantedTextView.MODE_LEFT)
            }
            else
            {
                itemView.product_price.text = formatRupiah.format(hn)
                itemView.product_price_diskon.visibility = View.INVISIBLE
                itemView.stvDiscount.visibility = View.INVISIBLE
            }

            if(f!=""&&ft!="" && f!=null && ft!=null){
                Glide.with(itemView.context).load(Url_Volley.url_website+"/ecommerce/assets/images/produk/" + f + ft)
                    .into(itemView.product_image)
            }

            itemView.addToCart.setOnClickListener{
                MCart.itemId=id
                if(st>0)
                {
                    var obj = QtyFragment() // fragment
                    var manager = (itemView.context as AppCompatActivity).fragmentManager //convert fragment ke activity dengan manager
                    obj.show(manager, "Qty")
                }
                else
                {
                    Toast.makeText(itemView.context, "Stok Barang Habis!", Toast.LENGTH_LONG).show()
                }



            }
            itemView.setOnClickListener(this)

        }
        override fun onClick(v: View?) {
            monlistener.onNoteClick(adapterPosition)
        }

    }
    interface OnNoteListener {
        fun onNoteClick(position: Int)
    }

//    fun setFilter(filterList: ArrayList<MItemDetail>) {
//        var arrayList:ArrayList<MItemDetail> = ArrayList()
//        arrayList.addAll(filterList)
//        notifyDataSetChanged()
//    }

}

