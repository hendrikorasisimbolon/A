package com.example.ta

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Fragment.NotFoundFragment
import com.example.ta.Model.MCart
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.activity_promo.*
import kotlinx.android.synthetic.main.item_promo.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class PromoAct : AppCompatActivity() {

    data class MPromo(
        var diskon: String,
        var id_promo: String,
        var ket_promo: String,
        var kode_promo: String,
        var max_pembelian: Int,
        var status: String
    )

    private lateinit var ui_hot:TextView
    var list:ArrayList<MPromo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo)
        initToolbar()
        var url = Url_Volley.url_website +"/udemy/get_all_promo.php?user_id="+MCart.user_id
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
            if(response.length()>0)
            {
                for(x in 0..response.length()-1)
                {
                    list.add(
                        MPromo(
                            response.getJSONObject(x).getString("diskon"),
                            response.getJSONObject(x).getString("id_promo"),
                            response.getJSONObject(x).getString("ket_promo"),
                            response.getJSONObject(x).getString("kode_promo"),
                            response.getJSONObject(x).getInt("max_pembelian"),
                            response.getJSONObject(x).getString("status")
                        )
                    )
                }
                var adp=PromoAdapter(this,list)
                rv_promo.layoutManager = LinearLayoutManager(this)
                rv_promo.adapter=adp
            }
            else {
                var frag = NotFoundFragment()
                var FM: FragmentManager? = supportFragmentManager
                var FT: FragmentTransaction = FM!!.beginTransaction()
                FT.replace(R.id.ly_rv, frag)
                FT.commit()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.getNavigationIcon()?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
        toolbar.title = "Promo"
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
        var actionView:View = MenuItemCompat.getActionView(menuItem)
        ui_hot = actionView.findViewById(R.id.hotlist_hot) as TextView
//        var i = Intent(this, OrderAct::class.java)
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

    class PromoAdapter (var context: Context, var promo:ArrayList<MPromo>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var v = LayoutInflater.from(context).inflate(R.layout.item_promo,parent,false)
            return PromoHolder(v)
        }

        override fun getItemCount(): Int = promo.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as PromoHolder).bindItem(
                promo[position].diskon,
                promo[position].kode_promo,
                promo[position].max_pembelian,
                promo[position].status
            )
        }
        class PromoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var locale = Locale("in", "ID")
            var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            fun bindItem(ds:String, kode:String, max:Int, st:String)
            {
                itemView.txt_diskon.text = "Diskon "+ ds +" %"
                itemView.txt_max.text = formatRupiah.format((max))
                itemView.txt_kode_diskon.text = kode
                if(st == "0")
                {
                    itemView.txt_kode_diskon.setOnClickListener {
                        var cllip:ClipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                    var isi = ClipData.newPlainText()
                        cllip.text = itemView.txt_kode_diskon.text.toString()
                        Toast.makeText(itemView.context,"Promo "+ kode +" sudah ter-copy",Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    itemView.txt_kode_diskon.setBackgroundResource(R.drawable.bg_round_bulat)
                    itemView.txt_kode_diskon.setOnClickListener {
                        Toast.makeText(itemView.context, "Promo " + kode + " sudah dipakai", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }
}

