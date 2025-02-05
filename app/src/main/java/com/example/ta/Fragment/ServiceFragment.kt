package com.example.ta.Fragment


import android.app.AlertDialog
import android.app.DialogFragment
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.FILL_PARENT
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ta.Adapter.ServiceAdapter
import com.example.ta.CheckoutAct
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.UserInfo
import com.example.ta.R
import com.example.ta.utils.UserSessionManager
import kotlinx.android.synthetic.main.item_ekpedisi.*





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
        dialog.window.setLayout(FILL_PARENT, FILL_PARENT)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var rv = v.findViewById<RecyclerView>(R.id.rv_ekpedisi_fr)
        session = UserSessionManager(activity!!.applicationContext)
        user = session.userDetails
        Log.e("eksp", CheckoutAct.eksp)
//        if(CheckoutAct.eksp == "pos"){
//            var adp = ServiceAdapter(activity!!,CheckoutAct.list_pos,this)
//            rv.layoutManager = LinearLayoutManager(activity!!)
//            rv.adapter = adp
//        }
//        if(CheckoutAct.eksp == "jne"){
//            var adp = ServiceAdapter(activity!!,CheckoutAct.list_jne,this)
//            rv.layoutManager = LinearLayoutManager(activity!!)
//            rv.adapter = adp
//        }
//        if(CheckoutAct.eksp == "pos"){
//            var adp = ServiceAdapter(activity!!,CheckoutAct.list_pos,this)
//            rv.layoutManager = LinearLayoutManager(activity!!)
//            rv.adapter = adp
//        }

        var adp = ServiceAdapter(activity!!,CheckoutAct.listEkspedisi,this)
        rv.layoutManager = LinearLayoutManager(activity!!)
        rv.adapter = adp


        Log.e("MskSrv", "asd")

        return  v
    }

    override fun onNoteClick(position: Int) {
        rd_eksp.isChecked
        CheckoutAct.service = position
        CheckoutAct.hasil_service = CheckoutAct.listEkspedisi[position].kode+ " "+ CheckoutAct.listEkspedisi[position].service + System.getProperty("line.separator")+
                "Harga Rp."+ CheckoutAct.listEkspedisi[position].tarif.toString()+ System.getProperty("line.separator")+ "Estimasi : "+
                CheckoutAct.listEkspedisi[position].estimasi+ " hari"

//        MTotalCart.total_harga -= CheckoutAct.harga_servicesblm
        MTotalCart.total_harga += CheckoutAct.listEkspedisi[position].tarif
        var intent: Intent = activity.intent
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NO_ANIMATION)
        activity.overridePendingTransition(0,0)
        activity.finish()

        activity.overridePendingTransition(0,0)
        startActivity(intent)
        CheckoutAct.harga_servicesblm =  CheckoutAct.listEkspedisi[position].tarif
        onStop()
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


}
