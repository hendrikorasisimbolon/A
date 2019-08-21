package com.example.ta

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
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.RiwayatAdapter
import com.example.ta.Fragment.NotFoundFragment
import com.example.ta.Model.MCart
import com.example.ta.Model.MRiwayat
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.activity_riwayat.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RiwayatAct : AppCompatActivity(), RiwayatAdapter.OnNoteListener {

    var list:ArrayList<MRiwayat> = ArrayList()
    private lateinit var ui_hot: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)
        initToolbar()
        getRiwayat(this)

    }

    override fun onBackPressed() {
        var i = Intent(this, MainActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i)
        super.onBackPressed()
    }
    fun getRiwayat(context:Context)
    {
        var t = 0
        var url = Url_Volley.url_website +"/udemy/get_history.php?user_id="+MCart.user_id
        var rq: RequestQueue = Volley.newRequestQueue(context)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->

            if(response.length() == 0)
            {
                var frag = NotFoundFragment()
                var FM: androidx.fragment.app.FragmentManager? = supportFragmentManager
                var FT: FragmentTransaction = FM!!.beginTransaction()
                FT.replace(R.id.hlma, frag)
                FT.commit()
            }
            else{
                for(x in 0..response.length()-1)
                {

                    var a = response.getJSONObject(x).getString("resi")
                    var c =""
                    if (a=="null")
                    {
                        c = "-"
                    }
                    if(response.getJSONObject(x).getInt("status")>1)
                    {
                        t += response.getJSONObject(x).getInt("total")}

                    list.add(
                        MRiwayat(
                            response.getJSONObject(x).getString("id_trans"),
                            response.getJSONObject(x).getString("user_id"),
                            response.getJSONObject(x).getString("created"),
                            response.getJSONObject(x).getInt("ongkir"),
                            response.getJSONObject(x).getString("kurir"),
                            response.getJSONObject(x).getString("service"),
                            response.getJSONObject(x).getInt("status"),
                            c,
                            response.getJSONObject(x).getString("total").toInt(),
                            response.getJSONObject(x).getInt("diskon")
                        )
                    )
                    Log.e("kurir", response.getJSONObject(x).getString("kurir"))
                }
                var adp = RiwayatAdapter(this,list, this)
                rc_history.layoutManager=LinearLayoutManager(this)
                rc_history.adapter=adp

                Log.e("riwayat",t.toString())

                var locale = Locale("in", "ID")
                var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
                totalKeluar.text = formatRupiah.format(t)
            }

        }, Response.ErrorListener { error ->
            Log.e("Riwayat", error.message)
//            Toast.makeText(context,error.message, Toast.LENGTH_LONG).show()
            Toast.makeText(context,"Anda belum membeli apapun", Toast.LENGTH_LONG).show()
        })
        rq.add(jar)


    }

    override fun onNoteClick(position: Int) {
        var intent = Intent(this, RiwayatDetailAact::class.java)
        Log.e("STATUS", list.get(position).status.toString())
        intent.putExtra("id_trans", list.get(position).id_Trans)
        intent.putExtra("status", list.get(position).status.toString())
        intent.putExtra("total", list.get(position).total.toString())
        intent.putExtra("kurir", list.get(position).kurir)
        intent.putExtra("service", list.get(position).service)
        intent.putExtra("resi", list.get(position).resi)
        intent.putExtra("ongkir", list.get(position).ongkir.toString())
        intent.putExtra("diskon", list.get(position).diskon.toString())
        intent.putExtra("created",list.get(position).created)
        startActivity(intent)
    }


    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener{
            var i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        toolbar.title = "Order History"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.white_transparency)
        Tools.setSystemBarLight(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart_setting, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
        Tools.changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.indigo_500))
        val menuItem: MenuItem = menu.findItem(R.id.aksi_cart)
        var actionView: View = MenuItemCompat.getActionView(menuItem)
        ui_hot = actionView.findViewById(R.id.hotlist_hot) as TextView
//        var i = Intent(this, MainActivity::class.java)
//        menuItem.intent = i
        setupBadge()

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.aksi_cart)
        {
            var i = Intent(this, OrderAct::class.java)
            startActivity(i)
        }
        if (item.itemId == R.id.action_search)
        {
            var i = Intent(this, ResultSearchAct::class.java)
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupBadge() {

        if (ui_hot != null) {
            if (MTotalCart.total_cart.toInt() == 0) {
                if (ui_hot.getVisibility() !== View.GONE) {
                    ui_hot.setVisibility(View.GONE)
                }
            } else {
//                ui_hot.setText(String.valueOf(Math.min(mCartItemCount, 99)))
                ui_hot.setText(Math.min(MTotalCart.total_cart.toInt(),99).toString())
                if (ui_hot.getVisibility() !== View.VISIBLE) {
                    ui_hot.setVisibility(View.VISIBLE)
                }
            }
        }
    }
}
