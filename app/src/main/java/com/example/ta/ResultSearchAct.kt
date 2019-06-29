package com.example.ta

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.ta.Fragment.SearchFragment
import com.example.ta.utils.Tools
import kotlinx.android.synthetic.main.activity_dashboard_grid_fab.*

class ResultSearchAct : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_search)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        var frag = SearchFragment()
        var FM: androidx.fragment.app.FragmentManager? = supportFragmentManager
        var FT: FragmentTransaction = FM!!.beginTransaction()
        FT.replace(R.id.halaman, frag)
        FT.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
        Tools.changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.indigo_500))
        return true
    }
}
