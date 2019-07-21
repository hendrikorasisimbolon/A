package com.example.ta

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.DaftarBRAdapter
import com.example.ta.Fragment.KonfrimPesananSampaiFragment
import com.example.ta.Fragment.NotFoundFragment
import com.example.ta.Model.MDaftarBR
import com.example.ta.Model.MCart
import com.example.ta.Model.Url_Volley
import com.example.ta.Model.UserInfo
import com.example.ta.utilss.Tools
import com.example.ta.utilss.UserSessionManager
import kotlinx.android.synthetic.main.activity_riwayat_detail_aact.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RiwayatDetailAact : AppCompatActivity() {



    lateinit var user: UserInfo
    lateinit var session: UserSessionManager
    var locale = Locale("in", "ID")
    var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)

    var list:ArrayList<MDaftarBR> = ArrayList()

    companion object
    {
        var id_trans = ""
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_detail_aact)
        session = UserSessionManager(applicationContext)
        initToolbar()
        user = session.userDetails

        id_trans = intent.getStringExtra("id_trans").toString()
        var status = intent.getStringExtra("status").toString()
        var total = intent.getStringExtra("total").toString()
        var kurir = intent.getStringExtra("kurir").toString()
        var service = intent.getStringExtra("service").toString()
        var resi = intent.getStringExtra("resi").toString()
        var ongkir = intent.getStringExtra("ongkir").toString()
        var diskon = intent.getStringExtra("diskon").toString()
        var created = intent.getStringExtra("created").toString()

        txt_invoice.text = id_trans
        var total_sel = total.toInt() + ongkir.toInt()
        t_subtotal.text = formatRupiah.format(total_sel)
        val nf = NumberFormat.getNumberInstance()
        nf.maximumFractionDigits = 0
        t_diskon.text = nf.format(diskon.toDouble())
        var tot = total.toDouble() * (diskon.toDouble()/100)
        var hasil = total_sel - tot
        t_total.text = hasil.toString()

        txt_ongkirr.text = formatRupiah.format(ongkir.toInt())
        Log.e("ErrorPaid", ongkir)
        if(status == "1")
        {
            txt_stat.text = "Belum Dibayar"
            txt_stat.setBackgroundResource(R.drawable.round_step1)
            btn_payy.visibility = View.VISIBLE
            btn_review.visibility = View.GONE
            btn_arr.visibility= View.GONE
            btn_belilagi.visibility = View.GONE
            btn_payy.setOnClickListener {
                var intent = Intent(this, PembayaranAct::class.java)
                intent.putExtra("service", service)
                intent.putExtra("kurir", kurir)
                intent.putExtra("total", total_sel.toString())
                intent.putExtra("ongkir", ongkir)

                startActivity(intent)
            }
        }
        else if(status == "2")
        {
            txt_stat.text = "Dibayar"
            txt_stat.setBackgroundResource(R.drawable.round_step2)
            btn_payy.visibility = View.GONE
            btn_review.visibility = View.GONE
            btn_arr.visibility= View.VISIBLE
            btn_belilagi.visibility = View.GONE
        }
        else if(status == "3")
        {
            txt_stat.text = "Dikirim"
            txt_stat.setBackgroundResource(R.drawable.round_success)
            btn_payy.visibility = View.GONE
            btn_review.visibility = View.GONE
            btn_arr.visibility= View.VISIBLE
            btn_belilagi.visibility = View.GONE
            btn_arr.setOnClickListener {
                var obj = KonfrimPesananSampaiFragment()
                var mann = this.fragmentManager
                obj.show(mann, "Smp")
            }
            var intent = Intent(this, RiwayatAct::class.java)
            startActivity(intent)
        }
        else if(status =="4")
        {
            txt_stat.text = "Sampai"
            txt_stat.setBackgroundResource(R.drawable.round_arrived)
            btn_payy.visibility = View.GONE
            btn_review.visibility = View.VISIBLE
            btn_arr.visibility= View.GONE
            btn_belilagi.visibility = View.GONE
            btn_review.setOnClickListener {
                var intent = Intent(this, TestiAct::class.java)
                startActivity(intent)
            }
        }
        else if(status =="5")
        {
            txt_stat.text = "Selesai"
            txt_stat.setBackgroundResource(R.drawable.bg_round_bulat)
            btn_payy.visibility = View.GONE
            btn_review.visibility = View.GONE
            btn_arr.visibility= View.GONE
            btn_belilagi.visibility = View.VISIBLE
            btn_belilagi.setOnClickListener {
               for(i in 0..list.count()-1)
               {
                   var url = Url_Volley.url_website +"/udemy/insert_cart.php?user_id="+ MCart.user_id +"&id_produk="+ list[i].idP +
                           "&total_qty=1"
                   var rq:RequestQueue = Volley.newRequestQueue(this)
                   var sr = StringRequest(Request.Method.GET,url, Response.Listener { response ->
                   }, Response.ErrorListener { error ->
                       Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                   })
                   rq.add(sr)
               }
                var intent = Intent(this, OrderAct::class.java)
                startActivity(intent)
            }
        }
        else
        {
            btn_payy.visibility = View.GONE
            btn_review.visibility = View.GONE
            btn_arr.visibility= View.GONE
            btn_belilagi.visibility = View.VISIBLE
            btn_belilagi.setBackgroundResource(R.drawable.round_notsucces)
            txt_stat.text = "Orderan dibatalkan"
            txt_stat.setBackgroundResource(R.drawable.round_notsucces)
            btn_belilagi.setOnClickListener {
                for(i in 0..list.count()-1)
                {
                    var url = Url_Volley.url_website +"/udemy/insert_cart.php?user_id="+ MCart.user_id +"&id_produk="+ list[i].idP +
                            "&total_qty=1"
                    var rq:RequestQueue = Volley.newRequestQueue(this)
                    var sr = StringRequest(Request.Method.GET,url, Response.Listener { response ->
                    }, Response.ErrorListener { error ->
                        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                    })
                    rq.add(sr)
                }
                var intent = Intent(this, OrderAct::class.java)
                intent.putExtra("id_trans", id_trans)
                startActivity(intent)
            }
        }

        if (kurir!="")
        {
            txt_kurirP.text = kurir +" - ("+ service +")"
        }
        if(resi!="")
        {
            txt_resiP.text = resi
        }
        Log.e("Kurir", kurir)
        txt_dateP.text = created
        txt_penerima.text = user.name
        txt_addressP.text = user.address
        txt_kote.text = user.nama_kota
        txt_prov.text = user.nama_provinsi
        daftarBr()
    }
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Order Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_10)
        Tools.setSystemBarLight(this)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    fun daftarBr(){
        Log.e("TRans", id_trans)
        var url = Url_Volley.url_website +"/udemy/get_history_detail.php?id_trans="+id_trans+"&user_id="+MCart.user_id
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->

            if(response.length() == 0)
            {
                var frag = NotFoundFragment()
                var FM: FragmentManager? = supportFragmentManager
                var FT: FragmentTransaction = FM!!.beginTransaction()
                FT.replace(R.id.ly_hlmn, frag)
                FT.commit()
            }
            else{
                for(x in 0..response.length()-1)
                {
                    list.add(
                        MDaftarBR(
                            response.getJSONObject(x).getString("produk_id"),
                            response.getJSONObject(x).getString("catatan"),
                            response.getJSONObject(x).getString("foto"),
                            response.getJSONObject(x).getString("foto_type"),
                            response.getJSONObject(x).getInt("harga"),
                            response.getJSONObject(x).getString("judul_produk"),
                            response.getJSONObject(x).getInt("subtotal"),
                            response.getJSONObject(x).getInt("total_qty")
                        )
                    )
                    Log.e("cata",response.getJSONObject(x).getString("catatan"))
                }

                var adp = DaftarBRAdapter(this,list)
                rc_detialP.layoutManager= LinearLayoutManager(this)
                rc_detialP.adapter=adp

            }

        }, Response.ErrorListener { error ->
            Log.e("RiwayatDetail", error.message)
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)
    }


}
