package com.example.ta.Fragment


import android.app.DialogFragment
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.MCart
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.OrderAct
import com.example.ta.R
import com.google.android.material.snackbar.Snackbar


class DltFragment : DialogFragment() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var v = inflater.inflate(R.layout.fragment_dlt, container, false)
        var btn = v.findViewById<Button>(R.id.btn_ya)

        btn.setOnClickListener{
            var url = url_website+"/udemy/delete_cart.php?user_id="+ MCart.user_id +"&produk_id="+ MCart.itemId +
                            "&total_qty=-1"
            var rq: RequestQueue = Volley.newRequestQueue(activity)
            var sr = StringRequest(Request.Method.GET,url, Response.Listener { response ->

                if (response == "1")
                {
                    Snackbar.make(activity.findViewById(R.id.coord),"1 barang telah dihapus dari cart anda", Snackbar.LENGTH_LONG).show()
                    MTotalCart.total_cart--
                }

            }, Response.ErrorListener { error ->
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            })
            rq.add(sr)

            var intent:Intent = activity.intent
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NO_ANIMATION)
            activity.overridePendingTransition(0,0)
            activity.finish()

            activity.overridePendingTransition(0,0)
            startActivity(intent)
        }

        return  v
    }


}
