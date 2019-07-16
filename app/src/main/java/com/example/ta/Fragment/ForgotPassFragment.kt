package com.example.ta.Fragment


import android.app.DialogFragment
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_forgot_pass.*

class ForgotPassFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_forgot_pass, container, false)
        var btn = v.findViewById<Button>(R.id.btn_forget)
        btn.setOnClickListener {
            var url = Url_Volley.url_website +"/udemy/lupa.php?email="+email_et.text.toString()
            var rq: RequestQueue = Volley.newRequestQueue(activity)
            var sr = StringRequest(Request.Method.GET,url, Response.Listener { response ->
//                Snackbar.make(activity!!.findViewById(R.id.rootView),response, Snackbar.LENGTH_LONG).show()
                Toast.makeText(activity, response.toString(), Toast.LENGTH_LONG).show()
                Log.e("errorfrgt",response)
            }, Response.ErrorListener { error ->
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            })
            rq.add(sr)
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
