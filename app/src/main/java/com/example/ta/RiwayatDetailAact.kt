package com.example.ta

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isInvisible
import com.example.ta.Model.UserInfo
import com.example.ta.utilss.Tools
import com.example.ta.utilss.UserSessionManager
import kotlinx.android.synthetic.main.activity_riwayat_detail_aact.*
import kotlinx.android.synthetic.main.item_ekpedisi.view.*

class RiwayatDetailAact : AppCompatActivity() {

    lateinit var user: UserInfo
    lateinit var session: UserSessionManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_detail_aact)
        session = UserSessionManager(applicationContext)
        initToolbar()
        user = session.userDetails

        var id_trans = intent.getStringExtra("id_trans").toString()
        var status = intent.getStringExtra("status").toString()
        var total = intent.getStringExtra("total").toString()
        var kurir = intent.getStringExtra("kurir").toString()
        var service = intent.getStringExtra("service").toString()
        var resi = intent.getStringExtra("resi").toString()
        var ongkir = intent.getStringExtra("ongkir").toString()
        var created = intent.getStringExtra("created").toString()

        txt_invoice.text = id_trans
        txtt_total.text = total

        if(status == "1")
        {
            txt_stat.text = "Not paid yet"
            txt_stat.setBackgroundResource(R.drawable.round_step1)
            btn_payy.visibility = View.VISIBLE
            btn_payy.setOnClickListener {
                var intent = Intent(this, PembayaranAct::class.java)
                intent.putExtra("service", service)
                intent.putExtra("kurir", kurir)
                intent.putExtra("total", (total+ongkir.toInt()).toString())
                intent.putExtra("ongkir", ongkir)

                startActivity(intent)
            }
        }
        else if(status == "2")
        {
            txt_stat.text = "Already Paid"
            txt_stat.setBackgroundResource(R.drawable.round_step2)
        }
        else if(status == "3")
        {
            txt_stat.text = "Has been sent"
            txt_stat.setBackgroundResource(R.drawable.round_success)
        }
        else if(status == "4")
        {
            txt_stat.text = "Your order was canceled"
            txt_stat.setBackgroundResource(R.drawable.round_notsucces)
        }

        if (kurir!="")
        {
            txt_kurirP.text = kurir +" - ("+ service +")"
        }

        txt_dateP.text = created
        txt_penerima.text = user.name
        txt_addressP.text = user.address
        txt_kote.text = user.nama_kota
        txt_prov.text = user.nama_provinsi

    }
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Order Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_10)
        Tools.setSystemBarLight(this)
        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this,RiwayatAct::class.java))
        }
    }
}
