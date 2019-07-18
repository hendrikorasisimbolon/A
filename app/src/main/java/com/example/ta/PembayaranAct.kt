package com.example.ta

import android.app.Activity
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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.MCart
import com.example.ta.Model.MPromo
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley
import com.example.ta.utilss.Tools
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import kotlinx.android.synthetic.main.activity_pembayaran.*
import kotlinx.android.synthetic.main.toolbar.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class PembayaranAct : AppCompatActivity() {
    private lateinit var ui_hot: TextView
    var config: PayPalConfiguration?=null
    var amount:Double = 0.0

    var locale = Locale("in", "ID")
    var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
    var list:ArrayList<MPromo> = ArrayList()
    var hasil = 0
    var serv=""
    var kurir=""
    var ongkir=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        initToolbar()

         serv = intent.getStringExtra("service").toString()
         kurir = intent.getStringExtra("kurir").toString()
         ongkir = intent.getStringExtra("ongkir").toString()

        txt_sblum.text =  formatRupiah.format( MTotalCart.total_harga)

        config = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Url_Volley.client_paypal)

        var i = Intent(this,PayPalService::class.java)
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
        startService(i)

        btn_check.setOnClickListener{
            get_promo()
        }


        btn_paynow.setOnClickListener{
            amount = hasil.toDouble() / 14000
            var payment = PayPalPayment(BigDecimal.valueOf(amount),"USD","Fashion Store App", PayPalPayment.PAYMENT_INTENT_SALE)
            var intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment)
            startActivityForResult(intent,123)
        }
    }

    fun get_promo(){
        var url = Url_Volley.url_website +"/udemy/promo.php?user_id="+MCart.user_id+"&kode_promo="+ txt_voucher.text.toString().toUpperCase()+"&total="+MTotalCart.total_harga.toString()
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET,url,null,Response.Listener { response ->
            if(response.length()>0)
            {
                for(i in 0..response.length()-1){
                    list.add(
                        MPromo(
                            response.getJSONObject(i).getInt("id_promo"),
                            response.getJSONObject(i).getString("kode_promo"),
                            response.getJSONObject(i).getString("ket_promo"),
                            response.getJSONObject(i).getInt("discount"),
                            response.getJSONObject(i).getInt("max_pembelian")
                        )
                    )
                }
                txt_dsco.text = "(Discount"+list[0].discount+"%)"
                var asa = MTotalCart.total_harga * (list[0].discount.toDouble() / 100)
                txt_disc.text = formatRupiah.format(asa)
                var ad = MTotalCart.total_harga - asa
                txt_ttl.text = formatRupiah.format(ad)
                hasil = ad.toInt()
            }
            else{
                txt_ttl.text = formatRupiah.format(MTotalCart.total_harga)
            }
        }, Response.ErrorListener {error ->
            Log.e("ERRORPEM", error.message)
            Toast.makeText(this, error.message.toString(),Toast.LENGTH_SHORT).show()
        })
        rq.add(jar)

    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Purchase"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_10)
        Tools.setSystemBarLight(this)
        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123){
            if(resultCode== Activity.RESULT_OK)
            {
                var intent = Intent(this, KonfirmasiPesananAct::class.java)
                intent.putExtra("service", serv)
                intent.putExtra("kurir", kurir)
                intent.putExtra("ongkir", ongkir)

                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        stopService(Intent(this, PayPalService::class.java))
        super.onDestroy()
    }
}
