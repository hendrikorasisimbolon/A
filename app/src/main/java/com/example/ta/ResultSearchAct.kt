package com.example.ta

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.ta.Fragment.SearchFragment
import com.example.ta.Model.MItemDetail

class ResultSearchAct : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_search)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        for (i in 0..MItemDetail.data.size-1){
            Log.e("data", MItemDetail.data[i].judul_produk)
        }

        var frag = SearchFragment()
        var FM: androidx.fragment.app.FragmentManager? = supportFragmentManager
        var FT: FragmentTransaction = FM!!.beginTransaction()
        FT.replace(R.id.halaman, frag)
        FT.commit()

    }

}
