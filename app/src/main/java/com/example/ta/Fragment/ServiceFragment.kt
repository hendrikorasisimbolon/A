package com.example.ta.Fragment


import android.app.AlertDialog
import android.app.DialogFragment
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.LinearGradient
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ta.Adapter.ServiceAdapter
import com.example.ta.Api.ApiServiceRO
import com.example.ta.Api.ApiUrl
import com.example.ta.CheckoutAct
import com.example.ta.Model.MService
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.UserInfo
import com.example.ta.Model.cost.ItemCost
import com.example.ta.R
import com.example.ta.utils.UserSessionManager
import com.google.gson.Gson

import kotlinx.android.synthetic.main.fragment_.view.*
import kotlinx.android.synthetic.main.item_ekpedisi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ServiceFragment : DialogFragment(), ServiceAdapter.OnNoteListener {

    private var progressDialog: ProgressDialog? = null
    private var alert: AlertDialog.Builder? = null
    private var ad: AlertDialog? = null
    lateinit var user: UserInfo
    lateinit var session: UserSessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_, container, false)

        var rv = v.findViewById<RecyclerView>(R.id.rv_ekpedisi_fr)
        session = UserSessionManager(activity!!.applicationContext)
        user = session.userDetails

        var adp = ServiceAdapter(activity!!,CheckoutAct.listEkspedisi,this)
        rv.layoutManager = LinearLayoutManager(activity!!)
        rv.adapter = adp
        Log.e("MskSrv", "asd")

        return  v
    }

    override fun onNoteClick(position: Int) {
        CheckoutAct.service = position
        rd_eksp.isChecked
//        var intent: Intent = activity.intent
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NO_ANIMATION)
//        activity.overridePendingTransition(0,0)
//        activity.finish()
//
//        activity.overridePendingTransition(0,0)
//        startActivity(intent)
        onStop()
    }


}
