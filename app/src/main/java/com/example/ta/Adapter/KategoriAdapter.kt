package com.example.ta.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.ta.Model.MKategori
import com.example.ta.R

class KategoriAdapter (var mCtx: Context, var resource:Int, var items:List<MKategori>)
    : ArrayAdapter<MKategori>( mCtx , resource , items ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )
        var judul : TextView = view.findViewById(R.id.jdul)


        var person : MKategori = items[position]
        judul.text = person.judul_subkat

        return view
    }
}