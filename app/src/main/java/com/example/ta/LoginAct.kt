package com.example.ta

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.core.content.ContextCompat
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.MCart
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.afterAnimationView
import kotlinx.android.synthetic.main.activity_login.rootView
import com.example.ta.Api.UserRepository
import com.example.ta.Model.MItemDetail
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.Model.UserInfo
import com.example.ta.utils.UserSessionManager
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import retrofit2.Call
import retrofit2.Callback
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


public class LoginAct : AppCompatActivity() {

    lateinit var session : UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        session = UserSessionManager(applicationContext)


        object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                bookITextView.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                rootView.setBackgroundColor(ContextCompat.getColor(this@LoginAct, R.color.colorSplashText))
                bookIconImageView.setImageResource(R.drawable.fashion)
                startAnimation()
            }
            override fun onTick(p0: Long) {}
        }.start()

        btn_Login.setOnClickListener{
            usrnameEditText.validator()
                .nonEmpty()
                .addErrorCallback { usrnameEditText.error = it }
                .check()

            passwdEditText.validator()
                .nonEmpty()
                .minLength(6)
//            .atleastOneUpperCase()
                .addErrorCallback { passwdEditText.error = it }
                .check()

            if (!usrnameEditText.text.isNullOrBlank() && !passwdEditText.text.isNullOrBlank())
            {
                var url = url_website + "/udemy/login.php?username=" + usrnameEditText.text.toString() + "&password=" + passwdEditText.text.toString()
                var rq: RequestQueue = Volley.newRequestQueue(this)
                var sr= StringRequest(Request.Method.GET,url, Response.Listener { response ->
                    if (response.equals("0")){
                        Toast.makeText(this,"Username and password is wrong", Toast.LENGTH_LONG).show()
                    }
                    else {
                        MCart.user_id = response.toString()
                        val userService = UserRepository.create()
                        userService.getUser(response.toString()).enqueue(object: Callback<List<UserInfo>> {
                            override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {

                            }

                            override fun onResponse(call: Call<List<UserInfo>>, res: retrofit2.Response<List<UserInfo>>) {
                                if(res.isSuccessful) {
                                    val data = res.body()
                                    data?.map {
                                        session.createUserLoginSession(UserInfo(
                                            it.id,it.name,it.username,passwdEditText.text.toString(),it.email,it.phone,it.nama_provinsi,it.id_provinsi,it.nama_kota,it.id_kota,it.address,it.umur,it.lahir,it.created_on
                                        ))
                                        getcart(it.id.toString())
                                    }
                                }
                            }

                        })
                        var i = Intent(this, MainActivity::class.java)
//                    i.putExtra("id",response.toString())
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(i)
                        MItemDetail.data = ArrayList()
                        MItemDetail.data = MItemDetail.getProducts(this)
                        finish()
                    }

                }, Response.ErrorListener { error ->
                    Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
                })
                rq.add(sr)

            }

        }
        signUP.setOnClickListener{
            var i = Intent(this,RegisterAct::class.java)
            startActivity(i)
        }

        Log.e("ID LOGIN", MCart.user_id)
        if (session.checkLogin())
            finish()

    }
    private fun startAnimation() {
        bookIconImageView.animate().apply {
            x(50f)
            y(100f)
            duration = 900
        }.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                afterAnimationView.visibility = VISIBLE
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })

        

    }

    override fun onBackPressed() {
        // Simply Do noting!
    }

    fun getcart(cart:String)
    {
        var locale = Locale("in", "ID")
        var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)

        var url1 = url_website +"/udemy/get_total_cart.php?user_id="+cart
        var rq1: RequestQueue = Volley.newRequestQueue(this)
        var jor = JsonObjectRequest(Request.Method.GET,url1,null, Response.Listener { response ->
            //            cart_size.text = response.getInt("banyak").toString()
            Log.e("Banyak Cart", response.getString("banyak"))
            MTotalCart.total_cart = response.getInt("banyak")
            MTotalCart.total_harga = response.getInt("jumlah")
            MTotalCart.total_berat = response.getInt("berat")
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq1.add(jor)
    }
}
