package com.example.ta.Fragment


import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.ta.Adapter.KategoriAdapter
import com.example.ta.ItemsAct
import com.example.ta.KategoriAct
import com.example.ta.KategoriAct.Companion.cat
import com.example.ta.KategoriAct.Companion.catName
import com.example.ta.Model.MKategori
import com.example.ta.R
import kotlinx.android.synthetic.main.toolbar.*

class PriaFragment : Fragment() {

    var kategori:ArrayList<MKategori> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var v =  inflater.inflate(R.layout.fragment_pria, container, false)
        var ls = v.findViewById<ListView>(R.id.list_cat_pria)
        setHasOptionsMenu(true)
        activity?.setTitle("Sub Category Pria")
        activity?.toolbar?.navigationIcon?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)


        var adp = KategoriAdapter(activity!!, R.layout.category, KategoriAct.kategoriP)

        ls.adapter = adp


        ls.setOnItemClickListener { parent, view, position, id ->

             cat = KategoriAct.kategoriP[position].id_subkat
             catName = KategoriAct.kategoriP[position].judul_subkat
            val intent = Intent(activity!!, ItemsAct::class.java)
//            intent.putExtra("cat", cat)
//            intent.putExtra("catname",catName)
            startActivity(intent)
        }
        return v
    }


}
