package com.example.ta.Fragment


import android.app.DialogFragment
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.MCart
import com.example.ta.Model.MTotalCart.Companion.total_cart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.R
import com.google.android.material.snackbar.Snackbar

@RequiresApi(Build.VERSION_CODES.HONEYCOMB)
class QtyFragment : DialogFragment() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var v = inflater.inflate(R.layout.fragment_qty, container, false)

        var et = v.findViewById<EditText>(R.id.et_qty)
        var btn= v.findViewById<Button>(R.id.btn_qty)

        btn.setOnClickListener{
            var url = url_website+"/udemy/insert_cart.php?user_id="+ MCart.user_id +"&id_produk="+ MCart.itemId +
                    "&total_qty=" + et.text.toString()
            var rq:RequestQueue = Volley.newRequestQueue(activity)
            var sr = StringRequest(Request.Method.GET,url, Response.Listener { response ->
                if (response == "1")
                {
//                    Snackbar.make(activity.findViewById(R.id.coordinator),"1 barang telah ditambahkan ke cart anda", Snackbar.LENGTH_LONG).show()
//                    Snackbar.make(activity.findViewById(R.id.coordinator),"1 barang telah diupdate di cart anda", Snackbar.LENGTH_LONG).show()
//                    pake toast
                    total_cart++
                }
                else
                {
//                    Snackbar.make(activity.findViewById(R.id.coordinator),"1 barang telah diupdate di cart anda", Snackbar.LENGTH_LONG).show()
                    Snackbar.make(activity.findViewById(R.id.coordinator),"1 barang telah ditambahkan ke cart anda", Snackbar.LENGTH_LONG).show()
                }
//                var i = Intent(activity,OrderAct::class.java)
//                startActivity(i)

            }, Response.ErrorListener { error ->
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            })

            rq.add(sr)
            var intent: Intent = activity.intent
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NO_ANIMATION)
            activity.overridePendingTransition(0,0)
            activity.finish()

            activity.overridePendingTransition(0,0)
            startActivity(intent)
//            onStop()
//            activity.finishAndRemoveTask()
//            activity.onBackPressed()
        }

        return v
    }


}
