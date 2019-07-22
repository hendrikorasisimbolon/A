package com.example.ta

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.MCart
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.activity_komplain.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.NullPointerException

class KomplainAct : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    data class Temp(
        var id_trandet:String,
        var judul:String,
        var id_pro:String
    )
    companion object{
        var id_trans = ""
    }
    var trans = ArrayList<String>()
    var produk = ArrayList<Temp>()
    private val IMAGE_REQUEST_CODE = 3
    private val STORAGE_PERMISSION_CODE = 123


    private lateinit var ui_hot:TextView
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_komplain)
        initToolbar()


        var id_pro =  intent.getStringExtra("id_produk").toString()
        var trans =  intent.getStringExtra("id_transdet").toString()
        et_transaksi.setText(id_pro)
        et_produk.setText(trans)



        img.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Choose image..."), IMAGE_REQUEST_CODE)
        }


    }

    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (view?.id) {
            1 -> showToast(message = "Spinner 2 Position:${position} and language: ${trans[position]}")
            else -> {
                showToast(message = "Spinner 1 Position:${position} and language: ${trans[position]}")
            }
        }
    }
    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.getNavigationIcon()?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
        toolbar.title = "Komplain"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.white_transparency)
        Tools.setSystemBarLight(this)
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) === PackageManager.PERMISSION_GRANTED
        )
            return

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart_setting, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
        Tools.changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.indigo_500))
        val menuItem: MenuItem = menu.findItem(R.id.aksi_cart)
        var actionView: View = MenuItemCompat.getActionView(menuItem)
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
}
