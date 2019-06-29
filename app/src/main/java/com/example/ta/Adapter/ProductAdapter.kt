package com.example.ta.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.ta.Model.MItems
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.android.synthetic.main.product_row_item.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

//class ProductAdapter (var context: Context, var list:ArrayList<MItems>) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    class ProductHolder(productView:View):RecyclerView.ViewHolder(productView)
//    {
//        fun bind(j:String,h:Double,f:String,ft:String)
//        {
//            var locale =  Locale("in", "ID")
//            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
//            itemView.product_name.text = j
//            itemView.product_price.text = formatRupiah.format(h)
//            Picasso.with(itemView.context).load("http://192.168.43.180/ecommerce/assets/images/produk/"+f+ft).into(itemView.product_image)
//        }
//    }

//}