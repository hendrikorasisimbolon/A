package com.example.ta.Fragment


import android.app.DialogFragment
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.CityAdapter
import com.example.ta.Adapter.ProvinceAdapter
import com.example.ta.Api.ApiServiceRO
import com.example.ta.Api.ApiUrl
import com.example.ta.Model.Url_Volley
import com.example.ta.Model.UserInfo
import com.example.ta.Model.kota.ItemCity
import com.example.ta.Model.provinsi.ItemProvince
import com.example.ta.Model.provinsi.Result
import com.example.ta.R
import com.example.ta.utilss.UserSessionManager
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class UpdateAddressFragment : DialogFragment() {

    lateinit var session: UserSessionManager
    lateinit var user: UserInfo
    private var alert: androidx.appcompat.app.AlertDialog.Builder? = null
    private var ad: androidx.appcompat.app.AlertDialog? = null
    private var searchList: EditText? = null
    private var mListView: ListView? = null
    private val ListProvince = ArrayList<Result>()
    private var adapter_province: ProvinceAdapter? = null
    private var progressDialog: ProgressDialog? = null
    private var adapter_city: CityAdapter? = null
    private val ListCity = ArrayList<com.example.ta.Model.kota.Result>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        session = UserSessionManager(activity!!.applicationContext)
        user = session.userDetails


        var province = v.findViewById<TextInputEditText>(R.id.ett_province)
        var city = v.findViewById<TextInputEditText>(R.id.ett_city)
        var address  = v.findViewById<TextInputEditText>(R.id.ett_addr)
        var update = v.findViewById<Button>(R.id.btn_update)

        province.setOnClickListener({popUpProvince(province,city)})

        city.setOnClickListener {
            try {
                if (province.tag==""){
                    province.error = "Please Chooice your province"
                } else {
                    popUpCity(city,province)
                }
            }catch (e:NullPointerException){
                province.error = "Please Chooice your province"
            }
        }

        update.setOnClickListener {
            var url = Url_Volley.url_website +"/udemy/update_address.php?id="+user.id+
                    "&address="+address.text.toString()+"&provinsi="+province.tag.toString()+"&kota="+city.tag.toString()
            var rq:RequestQueue = Volley.newRequestQueue(activity!!)
            var sr = StringRequest(Request.Method.GET,url,com.android.volley.Response.Listener { response ->
                    Log.e("sukses",response)
                    session.createUserLoginSession(
                        UserInfo(
                            user.id,
                            user.username,
                            user.name,
                            user.password,
                            user.email,
                            user.phone,
                            province.text.toString(),
                            province.tag.toString(),
                            city.text.toString(),
                            city.tag.toString(),
                            address.text.toString(),
                            user.umur,
                            user.lahir,
                            user.photo,
                            user.photo_type,
                            user.created_on
                        )
                    )



            }, com.android.volley.Response.ErrorListener { error ->
                Toast.makeText(activity!!,error.message, Toast.LENGTH_LONG).show()
            })
            rq.add(sr)

            var obj = F5Fragment() // fragment
            var mana = (activity!! as AppCompatActivity).fragmentManager
            obj.show(mana,"Rfs")
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

    fun popUpProvince(etProvince: EditText, etCity: EditText) {

//        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inflater = activity!!.layoutInflater

        val alertLayout = inflater.inflate(R.layout.custom_dialog_search, null)

        alert = androidx.appcompat.app.AlertDialog.Builder(activity!!)
        alert!!.setTitle("Daftar Provinsi")
        alert!!.setMessage("Pilih provinsi anda")
        alert!!.setView(alertLayout)
        alert!!.setCancelable(true)

        ad = alert!!.show()

        searchList = alertLayout.findViewById(R.id.searchItem) as EditText
        searchList!!.addTextChangedListener(MyTextWatcherProvince(searchList!!))
        searchList!!.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        mListView = alertLayout.findViewById(R.id.listItem) as ListView

        ListProvince.clear()
        adapter_province = ProvinceAdapter(activity!! as AppCompatActivity, ListProvince)
        mListView!!.isClickable = true

        mListView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val o = mListView!!.getItemAtPosition(i)
            val cn = o as Result

            etProvince.error = null
            etProvince.setText(cn.province)
            etProvince.tag = cn.provinceId

            etCity.setText("")
            etCity.tag = ""

            ad?.dismiss()
        }
        progressDialog = ProgressDialog(activity!!)
        progressDialog!!.setMessage("Please wait..")
        progressDialog!!.show()

        getProvince()
    }
    private inner class MyTextWatcherProvince(private val view: View) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(s: CharSequence, i: Int, before: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.searchItem -> adapter_province?.filter(editable.toString())
            }
        }
    }
    private fun getProvince() {

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiUrl.URL_ROOT_HTTPS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiServiceRO::class.java)
        val call = service.getProvince()

        call.enqueue(object : Callback<ItemProvince> {
            override fun onResponse(call: Call<ItemProvince>, response: Response<ItemProvince>) {

                progressDialog?.dismiss()
                Log.v("wow", "json : " + Gson().toJson(response))

                if (response.isSuccessful) {

                    val count_data = response.body()?.rajaongkir?.results?.size

                    for (a in 0..count_data!! - 1) {
                        val itemProvince = Result(
                            response.body()!!.rajaongkir!!.results?.get(a)?.provinceId,
                            response.body()!!.rajaongkir!!.results?.get(a)?.province

                        )

                        ListProvince.add(itemProvince)
                        mListView?.adapter = adapter_province
                    }

                    adapter_province?.setList(ListProvince)
                    adapter_province?.filter("")

                } else {
                    val error = "Error Retrive Data from Server !!!"
                    Toast.makeText(activity!!, error, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ItemProvince>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(activity!!, "Message : Error " + t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun popUpCity(etCity: EditText, etProvince: EditText) {

//        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inflater = activity!!.layoutInflater

        val alertLayout = inflater.inflate(R.layout.custom_dialog_search, null)

        alert = androidx.appcompat.app.AlertDialog.Builder(activity!!)
        alert!!.setTitle("Daftar Kota/Kabupaten")
        alert!!.setMessage("Pilih kota/kabupaten anda")
        alert!!.setView(alertLayout)
        alert!!.setCancelable(true)

        ad = alert!!.show()

        searchList = alertLayout.findViewById(R.id.searchItem) as EditText
        searchList!!.addTextChangedListener(MyTextWatcherCity(searchList!!))
        searchList!!.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        mListView = alertLayout.findViewById(R.id.listItem) as ListView

        ListCity.clear()
        adapter_city = CityAdapter(activity!! as AppCompatActivity, ListCity)
        mListView!!.isClickable = true

        mListView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val o = mListView!!.getItemAtPosition(i)
            val cn = o as com.example.ta.Model.kota.Result

            etCity.error = null
            etCity.setText(cn.cityName)
            etCity.tag = cn.cityId

            ad?.dismiss()
        }

        progressDialog = ProgressDialog(activity!!)
        progressDialog!!.setMessage("Please wait..")
        progressDialog!!.show()

        getCity(etProvince.tag.toString())

    }
    private inner class MyTextWatcherCity(private val view: View) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(s: CharSequence, i: Int, before: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.searchItem -> adapter_city?.filter(editable.toString())
            }
        }
    }

    fun getCity(id_province: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiUrl.URL_ROOT_HTTPS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiServiceRO::class.java)
        val call = service.getCity(id_province)

        call.enqueue(object : Callback<ItemCity> {
            override fun onResponse(call: Call<ItemCity>, response: Response<ItemCity>) {

                progressDialog?.dismiss()
                Log.v("wow", "json : " + Gson().toJson(response))

                if (response.isSuccessful) {

                    val count_data = response.body()?.rajaongkir?.results?.size

                    for (a in 0..count_data!! - 1) {
                        val itemProvince = com.example.ta.Model.kota.Result(
                            response.body()!!.rajaongkir?.results?.get(a)?.cityId,
                            response.body()!!.rajaongkir?.results?.get(a)?.provinceId,
                            response.body()!!.rajaongkir?.results?.get(a)?.province,
                            response.body()!!.rajaongkir?.results?.get(a)?.type,
                            response.body()!!.rajaongkir?.results?.get(a)?.cityName,
                            response.body()!!.rajaongkir?.results?.get(a)?.postalCode
                        )

                        ListCity.add(itemProvince)
                        mListView?.adapter = adapter_city
                    }

                    adapter_city?.setList(ListCity)
                    adapter_city?.filter("")

                } else {
                    val error = "Error Retrive Data from Server !!!"
                    Toast.makeText(activity!!, error, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ItemCity>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(activity!!, "Message : Error " + t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

}
