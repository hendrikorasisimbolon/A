package com.example.ta.Fragment


import android.app.DialogFragment
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.Url_Volley
import com.example.ta.R
import com.example.ta.RiwayatAct
import com.example.ta.RiwayatDetailAact
import com.example.ta.TestiAct

class KonfrimPesananSampaiFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_konfrim_pesanan_sampai, container, false)
        var btn_y = v.findViewById<Button>(R.id.btn_ya)
        var btn_t = v.findViewById<Button>(R.id.btn_tdk)

        btn_y.setOnClickListener {
            var url = Url_Volley.url_website+"/udemy/update_status.php?id_trans="+RiwayatDetailAact.id_trans+"&status=4"
            var rq: RequestQueue = Volley.newRequestQueue(activity)
            var jar= StringRequest(Request.Method.GET,url, Response.Listener { response ->
                Log.e("Responsedariselesai",response)
            }, Response.ErrorListener { error ->
                Toast.makeText(activity,error.message, Toast.LENGTH_LONG).show()
            })
            rq.add(jar)
            var intent = Intent(activity, RiwayatAct::class.java)
            startActivity(intent)
        }
        btn_t.setOnClickListener {
            onStop()
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }

}
