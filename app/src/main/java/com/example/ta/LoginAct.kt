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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.afterAnimationView
import kotlinx.android.synthetic.main.activity_login.rootView
import com.example.ta.Model.*
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.utilss.UserSessionManager
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
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
                .addErrorCallback { passwdEditText.error = it }
                .check()

            if (!usrnameEditText.text.isNullOrBlank() && !passwdEditText.text.isNullOrBlank())
            {
                var url = url_website + "/udemy/login.php?username=" + usrnameEditText.text.toString() + "&password=" + passwdEditText.text.toString()
                var rq: RequestQueue = Volley.newRequestQueue(this)
                var sr= JsonArrayRequest(Request.Method.GET,url, null, Response.Listener { response ->
                    if (response.length()>0){
                        for(x in 0..response.length()-1)
                        {
                            Log.e("MAS",response.getJSONObject(x).getString("name"))
                            MCart.user_id =  response.getJSONObject(x).getInt("id").toString()
                            session.createUserLoginSession(
                                UserInfo(
                                    response.getJSONObject(x).getInt("id"),
                                    response.getJSONObject(x).getString("name"),
                                    response.getJSONObject(x).getString("username"),
                                    response.getJSONObject(x).getString("password"),
                                    response.getJSONObject(x).getString("email"),
                                    response.getJSONObject(x).getString("phone"),
                                    response.getJSONObject(x).getString(("nama_provinsi")),
                                    response.getJSONObject(x).getString("id_provinsi"),
                                    response.getJSONObject(x).getString("nama_kota"),
                                    response.getJSONObject(x).getString("id_kota"),
                                    response.getJSONObject(x).getString("address"),
                                    response.getJSONObject(x).getString("umur"),
                                    response.getJSONObject(x).getString("lahir"),
                                    response.getJSONObject(x).getString("photo"),
                                    response.getJSONObject(x).getString("photo_type"),
                                    response.getJSONObject(x).getInt("created_on"))
                            )
                        }
                    }

                    getcart(MCart.user_id)
                    MCart.password = passwdEditText.text.toString()
                    var i = Intent(this, MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    MItemDetail.data = ArrayList()
                    MItemDetail.data = MItemDetail.getProducts(this)
                    finish()

                }, Response.ErrorListener { error ->
                    Log.e("ErrorBawaan",error.message)
//                    Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
                    Toast.makeText(this,"Username and password is wrong", Toast.LENGTH_LONG).show()
                })
                rq.add(sr)



            }

        }
        signUP.setOnClickListener{
            var i = Intent(this,RegisterAct::class.java)
            startActivity(i)
        }

        Log.e("ID LOGIN", MCart.user_id)
//        session.checkLogin()

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
