package com.example.ta.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ta.Model.MTesti
import com.example.ta.Model.Url_Volley
import com.example.ta.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_test.view.*

class TestiAdapter (var context: Context, var daftar:ArrayList<MTesti>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_test, parent, false)
        return TestiHolder(layout)
    }

    override fun getItemCount(): Int = daftar.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TestiHolder).bindItem(
            daftar[position].name,
            daftar[position].photo,
            daftar[position].photo_type,
            daftar[position].testi,
            daftar[position].date_crate,
            daftar[position].rating
        )
    }
    class TestiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(nm:String, ft:String, ftt:String, tsti:String, dt:String, rt:Float)
        {
            itemView.txt_usr.text = nm
            itemView.txt_isi_test.text = tsti
            itemView.txt_date_testi.text = dt
            itemView.rt_testi.rating = rt
            if(ft!="")
            {
                Picasso.with(itemView.context).load(Url_Volley.url_website+"/ecommerce/assets/images/user/" + ft + ftt)
                    .into(itemView.foto_dp)
            }

        }

    }


}

