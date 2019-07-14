package com.example.ta.Adapter

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.Html
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.ta.Model.MBlog
import com.example.ta.Model.Url_Volley
import com.example.ta.R
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.item_blog.view.*

import kotlin.collections.ArrayList

//public class BlogAdapter(var context: Context, var list:ArrayList<MBlog>, var monlistener: OnNoteListener) :
public class BlogAdapter(var context: Context, var list:ArrayList<MBlog>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        var v:View = LayoutInflater.from(context).inflate(R.layout.item_blog,p0,false)
        return BlogHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        (p0 as BlogHolder).bind(
            list[p1].id_blog,
            list[p1].judul_blog,
            list[p1].isi_blog,
            list[p1].created,
            list[p1].modified_by,
            list[p1].foto,
            list[p1].foto_type
//            this.monlistener
            )
    }

//    public class BlogHolder (itemView:View) :RecyclerView.ViewHolder(itemView), View.OnClickListener
    public class BlogHolder (itemView:View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
    {
//        lateinit var monlistener: OnNoteListener

//        fun bind(id:Int, j:String, i:String, c: String, m:String, f:String, ft:String,monlistener: OnNoteListener) {
@TargetApi(Build.VERSION_CODES.N)
@RequiresApi(Build.VERSION_CODES.N)
fun bind(id:Int, j:String, i:String, c: String, m:String, f:String, ft:String) {
//            this.monlistener = monlistener
            itemView.name.text = j
            itemView.description.text = Html.fromHtml(i,Html.FROM_HTML_MODE_COMPACT)
            Picasso.with(itemView.context).load(Url_Volley.url_website+"/ecommerce/assets/images/blog/"+f+ft)
                .into(itemView.image)
        }


//        override fun onClick(v: View?) {
//            monlistener.onNoteClick(this.adapterPosition)
//        }

    }
//    interface OnNoteListener {
//        fun onNoteClick(position: Int)
//    }


}
