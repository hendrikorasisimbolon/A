package com.example.ta

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Fragment.PriaFragment
import com.example.ta.Fragment.WanitaFragment
import com.example.ta.Model.MKategori
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley
import com.example.ta.utilss.Tools
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.toolbar.*
import java.util.ArrayList

class KategoriAct : AppCompatActivity() {

    companion object{
        var cat:String = "0"
        var catName:String = ""
        var kategoriP:ArrayList<MKategori> = ArrayList()
        var kategoriW:ArrayList<MKategori> = ArrayList()
        fun getCat(context: Context){
            var url = Url_Volley.url_website +"/udemy/get_category.php"
            var rq: RequestQueue = Volley.newRequestQueue(context)
            var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
                kategoriW.clear()
                kategoriP.clear()
                for (x in 0 .. response.length()-1)
                {
//                    Log.e("Kategori", response.getJSONObject(x).getString("id_kat"))
                    if(response.getJSONObject(x).getString("id_kat") == "1")
                    {
                        kategoriP.add(MKategori(
                            response.getJSONObject(x).getString("id_subkategori"),
                            response.getJSONObject(x).getString("id_kat"),
                            response.getJSONObject(x).getString("judul_subkategori")
                        ))
                    }
                    else
                    {
                        kategoriW.add(MKategori(
                            response.getJSONObject(x).getString("id_subkategori"),
                            response.getJSONObject(x).getString("id_kat"),
                            response.getJSONObject(x).getString("judul_subkategori")
                        ))
                    }
                }

            }, Response.ErrorListener { error ->
                Toast.makeText(context,error.message, Toast.LENGTH_LONG).show()
            })
            rq.add(jar)
        }
    }

    private lateinit var ui_hot:TextView
    var view_pager:ViewPager ?= null
    private var tab_layout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori)
        initToolbar()
        initComponent()
        var url = Url_Volley.url_website +"/udemy/get_category.php"
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
            kategoriW.clear()
            kategoriP.clear()
            for (x in 0 .. response.length()-1)
            {
//                Log.e("Kategori", response.getJSONObject(x).getString("id_kat"))
                if(response.getJSONObject(x).getString("id_kat") == "1")
                {
                    kategoriP.add(MKategori(
                        response.getJSONObject(x).getString("id_subkategori"),
                        response.getJSONObject(x).getString("id_kat"),
                        response.getJSONObject(x).getString("judul_subkategori")
                    ))
                }
                else
                {
                    kategoriW.add(MKategori(
                        response.getJSONObject(x).getString("id_subkategori"),
                        response.getJSONObject(x).getString("id_kat"),
                        response.getJSONObject(x).getString("judul_subkategori")
                    ))
                }
            }

        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)

    }


    @SuppressLint("ResourceAsColor")
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbark)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Kategori")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.getNavigationIcon()?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        Tools.setSystemBarColor(this, R.color.white_transparency)
        Tools.setSystemBarLight(this)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(WanitaFragment(), "Wanita")
        adapter.addFragment(PriaFragment(), "Pria")
        viewPager.adapter = adapter
    }

    private fun initComponent() {
        view_pager = findViewById(R.id.view_pager)
        setupViewPager(view_pager!!)

        tab_layout = findViewById(R.id.tab_layout)
        tab_layout!!.setupWithViewPager(view_pager)
    }

    private inner class SectionsPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {


        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()


        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getCount(): Int= mFragmentList.size

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
}
