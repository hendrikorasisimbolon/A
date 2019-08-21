package com.example.ta

//import com.example.ta.Adapter.SliderAdapter
/*
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
*/

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.*
import com.example.ta.Model.MTotalCart.Companion.total_cart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.utilss.Tools
import com.example.ta.utilss.UserSessionManager
import com.google.android.material.navigation.NavigationView
import com.synnapps.carouselview.ViewListener
import io.customerly.Customerly
import kotlinx.android.synthetic.main.action_bar_notifitcation_icon.*
import kotlinx.android.synthetic.main.activity_dashboard_grid_fab.toolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.image_slider_layout_item.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var session:UserSessionManager
    lateinit var user: UserInfo
    private lateinit var ui_hot:TextView
    private var progressDialog: ProgressDialog? = null


    var sampleImages =
        intArrayOf(R.drawable.sl1, R.drawable.sl2, R.drawable.sl3)
    var desk: Array<String> = arrayOf("Banner 1", "Banner2", "Banner3")
//    var desk: Array<String> = emptyArray()

    var img:ArrayList<SliderInfo> = ArrayList()

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        KategoriAct.getCat(this)
        getcart()
        initToolbar()
        initNavigationMenu()
        MRatingBarang.list = MRatingBarang.getAllRating(this)
//        MRatingBarang.daftarRating = MRatingBarang.getAlgo(this)


        session = UserSessionManager(applicationContext)
        user = session.userDetails


        session.checkLogin()
//        getcart()

//        var url = url_website +"/udemy/get_total_cart.php?user_id="+MCart.user_id
//        var rq: RequestQueue = Volley.newRequestQueue(this)
//        var jor = JsonObjectRequest(Request.Method.GET,url,null, Response.Listener { response ->
////            cart_size.text = response.getInt("banyak").toString()
//            Log.e("Banyak Cart", response.getString("banyak"))
//            total_cart = response.getInt("banyak")
//        }, Response.ErrorListener { error ->
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//        })
//        rq.add(jor)

//        var uri = "http://192.168.43.180/udemy/get_slider.php"
//        var rqq: RequestQueue = Volley.newRequestQueue(this)
//        var jar = JsonArrayRequest(Request.Method.GET,uri,null, Response.Listener { response ->
//            for (i in 0 .. response.length()-1)
//            {
//                Log.e("JSON", "http://192.168.43.180/ecommerce/assets/images/slider/"+response.getJSONObject(i).getString("foto")+response.getJSONObject(i).getString("foto_type"))
//                img.add(
//                    SliderInfo(
//                        response.getJSONObject(i).getString("judul_slider"),"http://192.168.43.180/ecommerce/assets/images/slider/"+response.getJSONObject(i).getString("foto")+response.getJSONObject(i).getString("foto_type")
//                    )
//                )
//            }
//        }, Response.ErrorListener { error ->
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//        })
//        rqq.add(jar)


//        imageSlider.sliderAdapter = SliderAdapter(this,img)
//        imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP)
//        imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
//        imageSlider.scrollIndicators = 2
//        imageSlider.startAutoCycle()

        carouselView.pageCount = sampleImages.count();
//        carouselView.setPageCount(img.size);

        carouselView.setViewListener(viewListener);
        carouselView.setImageClickListener { position ->
            Toast.makeText(this, "Click item at :" + position, Toast.LENGTH_LONG).show()
        }

        btn_kategori.setOnClickListener{
            var i = Intent(this,KategoriAct::class.java)
            startActivity(i)
        }

        btn_blog.setOnClickListener {
            var i = Intent(this,BlogAct::class.java)
            startActivity(i)
        }
        btn_all.setOnClickListener{
            var i = Intent(this,ProductAllAct::class.java)
//            var i = Intent(this,ItemAllAct::class.java)
            startActivity(i)
        }
        btn_rekomendasi.setOnClickListener {
            var i = Intent(this,RekomendasiAct::class.java)
            startActivity(i)
        }
        btn_promo_menu.setOnClickListener{
            var i = Intent(this,PromoAct::class.java)
            startActivity(i)
        }
        btn_komplain.setOnClickListener {
            var i = Intent(this,DaftarKomplainAct::class.java)
            startActivity(i)
        }


    }

    var viewListener: ViewListener = ViewListener { position ->
        val customView = layoutInflater.inflate(R.layout.image_slider_layout_item, null)
        val myImageView = customView.iv_auto_image_slider
        myImageView.setImageResource(sampleImages[position])
//        Picasso.with(applicationContext).load(img[position].site).into(iv_auto_image_slider)
//        customView.tv_auto_image_slider.setText(position.toString())
        customView.tv_auto_image_slider.setText(desk[position])
        return@ViewListener customView

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

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.getNavigationIcon()?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.title = "Fashion Store"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.white_transparency)
        Tools.setSystemBarLight(this)
    }

     fun initNavigationMenu() {
        val nav_view = findViewById(R.id.nav_view) as NavigationView
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }

        drawer.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener { item ->
//            Toast.makeText(applicationContext, item.title.toString() + " Selected", Toast.LENGTH_SHORT).show()
            if (item.itemId == R.id.nav_profile){
                var i = Intent(this,ProfileAct::class.java)
                startActivity(i)
            }
            if (item.itemId == R.id.nav_kategori){
                var i = Intent(this,KategoriAct::class.java)
                startActivity(i)
            }
            if (item.itemId == R.id.nav_blog)
            {
                var i = Intent(this,BlogAct::class.java)
                startActivity(i)
            }
            if (item.itemId == R.id.nav_keluar)
            {
                session.logoutUser()
                Customerly.logoutUser()
            }
            if(item.itemId == R.id.nav_riwayat){
                var i = Intent(this,RiwayatAct::class.java)
                startActivity(i)
            }
           if(item.itemId == R.id.nav_chat){
               Customerly.openSupport(this)
            }
            drawer.closeDrawers()
            true
        }

        // open drawer at start
        drawer.openDrawer(GravityCompat.START)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart_setting_search, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
        Tools.changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.indigo_500))

        //Cart
        val menuItem:MenuItem = menu.findItem(R.id.aksi_cart)
        var actionView:View = MenuItemCompat.getActionView(menuItem)
        ui_hot = actionView.findViewById(R.id.hotlist_hot) as TextView
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
            Log.e("Ui hOT TIDAK DULL", "true")
            if (total_cart == 0) {
                if (ui_hot.getVisibility() !== View.GONE) {
                    ui_hot.setVisibility(View.GONE)
                    Log.e("total cart kosong", total_cart.toString())
                }
            } else {
//                ui_hot.setText(String.valueOf(Math.min(mCartItemCount, 99)))
                ui_hot.setText(Math.min(total_cart,99).toString())
                if (ui_hot.getVisibility() !== View.VISIBLE) {
                    ui_hot.setVisibility(View.VISIBLE)
                }
            }
        }
    }

    fun getcart()
    {
        var locale = Locale("in", "ID")
        var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)

        var url1 = url_website +"/udemy/get_total_cart.php?user_id="+ MCart.user_id
        var rq1: RequestQueue = Volley.newRequestQueue(this)
        var jor = JsonObjectRequest(Request.Method.GET,url1,null, Response.Listener { response ->
            //            cart_size.text = response.getInt("banyak").toString()
            Log.e("Banyak Cart", response.getString("banyak"))
            total_cart = response.getInt("banyak")
            MTotalCart.total_harga = response.getInt("jumlah")
            MTotalCart.total_berat = response.getInt("berat")
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq1.add(jor)
        txt_total?.text = formatRupiah.format(MTotalCart.total_harga)
        hotlist_hot?.text = Math.min(total_cart,99).toString()
    }



}
