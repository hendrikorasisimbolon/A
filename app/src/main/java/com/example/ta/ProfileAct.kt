package com.example.ta

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.MCart
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.MTotalCart.Companion.total_cart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.Model.UserInfo
import com.example.ta.utils.Tools
import com.example.ta.utils.UserSessionManager
import kotlinx.android.synthetic.main.action_bar_notifitcation_icon.*
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import java.text.NumberFormat
import java.util.*


@Suppress("DEPRECATION")
class ProfileAct : AppCompatActivity() {


    lateinit var user:UserInfo
    lateinit var session:UserSessionManager
    private lateinit var ui_hot:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        session = UserSessionManager(applicationContext)
        initToolbar()

        user = session.userDetails
        name_title.text = user.name.capitalize()
        nama_user.text = user.name.capitalize()
        email_user.text = user.email
        phone_user.text = "+62" + user.phone
        location_user.text = user.nama_kota + ", " +user.nama_provinsi
//        var date = Date(user.created_on.toLong()*1000L)
//        var df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
//        created_user.text = df.format(date)
        txt_lahir.text = user.lahir
        txt_age.text = user.umur

        address_user.text = user.address

        edit_user.setOnClickListener{
            var i = Intent(this,EditProfileAct::class.java)
            startActivity(i)
        }

//        var url = url_website+"/udemy/get_total_cart.php?user_id="+ MCart.user_id
//        var rq: RequestQueue = Volley.newRequestQueue(this)
//        var jor = JsonObjectRequest(Request.Method.GET,url,null, Response.Listener { response ->
//            //            cart_size.text = response.getInt("banyak").toString()
//            Log.e("Banyak Cart", response.getString("banyak"))
//            total_cart = response.getInt("banyak")
//        }, Response.ErrorListener { error ->
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//        })
//        rq.add(jor)

    }

    override fun onStart() {
        getcart()
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                try {
                    recreate()
                } catch (e: Exception) {}
                handler.postDelayed(this, 300000)
            }
        }
        handler.postDelayed(runnable, 300000)
        super.onStart()
    }

    fun getcart()
    {
        var locale = Locale("in", "ID")
        var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)

        var url1 = url_website +"/udemy/get_total_cart.php?user_id="+ MCart.user_id
        var rq1: RequestQueue = Volley.newRequestQueue(this)
        var jor = JsonObjectRequest(Request.Method.GET,url1,null, com.android.volley.Response.Listener { response ->
            //            cart_size.text = response.getInt("banyak").toString()
            Log.e("Banyak Cart", response.getString("banyak"))
            MTotalCart.total_cart = response.getInt("banyak")
            MTotalCart.total_harga = response.getInt("jumlah")
        }, com.android.volley.Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq1.add(jor)
        txt_total?.text = formatRupiah.format(MTotalCart.total_harga)
        hotlist_hot?.text = Math.min(MTotalCart.total_cart,99).toString()
    }


    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.getNavigationIcon()?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener{
           onBackPressed()
        }
        toolbar.title = "Profile"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.white_transparency)
        Tools.setSystemBarLight(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart_setting, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
        Tools.changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.indigo_500))
        val menuItem:MenuItem = menu.findItem(R.id.aksi_cart)
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
            if (total_cart.toInt() == 0) {
                if (ui_hot.getVisibility() !== View.GONE) {
                    ui_hot.setVisibility(View.GONE)
                }
            } else {
//                ui_hot.setText(String.valueOf(Math.min(mCartItemCount, 99)))
                ui_hot.setText(Math.min(total_cart.toInt(),99).toString())
                if (ui_hot.getVisibility() !== View.VISIBLE) {
                    ui_hot.setVisibility(View.VISIBLE)
                }
            }
        }
    }

    override fun onBackPressed() {
        var i = Intent(this,MainActivity::class.java)
        startActivity(i)
    }

}
