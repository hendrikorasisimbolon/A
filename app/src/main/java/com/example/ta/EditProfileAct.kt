package com.example.ta

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.CityAdapter
import com.example.ta.Adapter.ProvinceAdapter
import com.example.ta.Api.ApiServiceRO
import com.example.ta.Api.ApiUrl
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.Model.UserInfo
import com.example.ta.Model.kota.ItemCity
import com.example.ta.Model.provinsi.ItemProvince
import com.example.ta.Model.provinsi.Result
import com.example.ta.RulesExtra.EmailValid
import com.example.ta.RulesExtra.NameValid
import com.example.ta.RulesExtra.UsernameValid
import com.example.ta.utils.Tools
import com.example.ta.utils.UserSessionManager
import com.google.gson.Gson
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.etToCity
import kotlinx.android.synthetic.main.activity_edit_profile.etToProvince
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class EditProfileAct : AppCompatActivity() {

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
    var yearU:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        session = UserSessionManager(applicationContext)

        initToolbar()

        user = session.userDetails

        et_usrname.setText(user.username)
        et_name.setText(user.name)
        et_address.setText(user.address)
        et_email.setText(user.email)
        et_phone.setText(user.phone)
        et_age.setText(user.lahir)
        etToProvince.setText(user.nama_provinsi)
        etToProvince.tag = user.id_provinsi
        etToCity.setText(user.nama_kota)
        etToCity.tag = user.id_kota
        et_oldpass.setText(user.password)

        etToProvince.setOnClickListener({ popUpProvince(etToProvince, etToCity) })

        etToCity.setOnClickListener {
            try {
                if (etToProvince.tag == "") {
                    etToProvince.error = "Please chooise your province"
                } else {
                    popUpCity(etToCity, etToProvince)
                }

            } catch (e: NullPointerException) {
                etToProvince.error = "Please chooise your to province"
            }
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        var lahir = year - yearU

        btn_save.setOnClickListener{
            var urll = url_website+"/udemy/update_user.php?id="+ user.id+
                    "&name="+ et_name.text.toString() +"&username="+ et_usrname.text.toString() +"&email="+ et_email.text.toString()+
                    "&password="+ et_newpass.text.toString() + "&phone="+ et_phone.text.toString() +"&address="+ et_address.text +
                    "&lahir="+ et_age.text.toString() +"&provinsi="+ etToProvince.tag +"&kota="+ etToCity.tag
            var rq: RequestQueue = Volley.newRequestQueue(this)
            var sr = StringRequest(Request.Method.GET,urll,com.android.volley.Response.Listener { response ->
                if(response == "sukses")
                {
                    if (user.password == et_oldpass.text.toString()) {
                        var i = Intent(this, ProfileAct::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(i)
                        session.createUserLoginSession(
                            UserInfo(
                                user.id,
                                et_usrname.text.toString(),
                                et_name.text.toString(),
                                et_newpass.text.toString(),
                                et_email.text.toString(),
                                et_phone.text.toString(),
                                etToProvince.text.toString(),
                                etToProvince.tag.toString(),
                                etToCity.text.toString(),
                                etToCity.tag.toString(),
                                et_address.text.toString(),
                                lahir.toString(),
                                et_age.text.toString(),
                                user.created_on
                            )
                        )
                        Log.e("user",session.userDetails.password)

                    }
                    else{
                        Toast.makeText(this,"Wrong Old Password!", Toast.LENGTH_LONG).show()
                    }
                }
                else {
                    var part: List<String> = response.split(" ")
                    for (i in 0 .. part.size-1){
                        if (part[i]=="-1")
                        {
                            RegisterAct.username_ = et_usrname.text.toString()
                            Toast.makeText(this,"Username already used", Toast.LENGTH_LONG).show()
                            et_usrname.validator()
                                .addRule(UsernameValid())
                                .addErrorCallback { et_usrname.error = it }
                                .check()
                        }
                        if (part[i]=="-2"){
                            RegisterAct.name_ = et_name.text.toString()
                            Toast.makeText(this,"Name already used", Toast.LENGTH_LONG).show()
                            et_name.validator()
                                .addRule(NameValid())
                                .addErrorCallback { et_name.error = it }
                                .check()
                        }
                        if(part[i]=="-3"){
                            RegisterAct.email_ = et_email.text.toString()
                            Toast.makeText(this,"Email already used", Toast.LENGTH_LONG).show()
                            et_email.validator()
                                .addRule(EmailValid())
                                .addErrorCallback { et_email.error =it }
                                .check()
                        }
                    }
                }
            },com.android.volley.Response.ErrorListener { error ->
                Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
                Log.e("wow", error.message)
            })
            rq.add(sr)
            validasi()
        }

    }


    fun popUpProvince(etProvince: EditText, etCity: EditText) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val alertLayout = inflater.inflate(R.layout.custom_dialog_search, null)

        alert = androidx.appcompat.app.AlertDialog.Builder(this@EditProfileAct)
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
        adapter_province = ProvinceAdapter(this@EditProfileAct, ListProvince)
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
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Please wait..")
        progressDialog!!.show()

        getProvince()
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        var lahir = year - yearU
//
//        btn_save.setOnClickListener{
//                var urll = url_website+"/udemy/update_user.php?id="+ user.id+
//                        "&name="+ et_name.text.toString() +"&username="+ et_usrname.text.toString() +"&email="+ et_email.text.toString()+
//                        "&password="+ et_newpass.text.toString() + "&phone="+ et_phone.text.toString() +"&address="+ et_address.text +
//                        "&lahir="+ et_age.text.toString() +"&provinsi="+ etProvince.tag +"&kota="+ etCity.tag
//                var rq: RequestQueue = Volley.newRequestQueue(this)
//                var sr = StringRequest(Request.Method.GET,urll,com.android.volley.Response.Listener { response ->
//                        if(response == "sukses")
//                        {
//                            if (user.password == et_oldpass.text.toString()) {
//                                var i = Intent(this, ProfileAct::class.java)
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                                startActivity(i)
//                                session.createUserLoginSession(
//                                    UserInfo(
//                                        user.id,
//                                        et_usrname.text.toString(),
//                                        et_name.text.toString(),
//                                        et_newpass.text.toString(),
//                                        et_email.text.toString(),
//                                        et_phone.text.toString(),
//                                        etProvince.text.toString(),
//                                        etProvince.tag.toString(),
//                                        etCity.text.toString(),
//                                        etCity.tag.toString(),
//                                        et_address.text.toString(),
//                                        lahir.toString(),
//                                        et_age.text.toString(),
//                                        user.created_on
//                                    )
//                                )
//                                Log.e("user",session.userDetails.password)
//
//                            }
//                            else{
//                                Toast.makeText(this,"Wrong Old Password!", Toast.LENGTH_LONG).show()
//                            }
//                        }
//                    else {
//                            var part: List<String> = response.split(" ")
//                            for (i in 0 .. part.size-1){
//                                if (part[i]=="-1")
//                                {
//                                    RegisterAct.username_ = et_usrname.text.toString()
//                                    Toast.makeText(this,"Username already used", Toast.LENGTH_LONG).show()
//                                    et_usrname.validator()
//                                        .addRule(UsernameValid())
//                                        .addErrorCallback { et_usrname.error = it }
//                                        .check()
//                                }
//                                if (part[i]=="-2"){
//                                    RegisterAct.name_ = et_name.text.toString()
//                                    Toast.makeText(this,"Name already used", Toast.LENGTH_LONG).show()
//                                    et_name.validator()
//                                        .addRule(NameValid())
//                                        .addErrorCallback { et_name.error = it }
//                                        .check()
//                                }
//                                if(part[i]=="-3"){
//                                    RegisterAct.email_ = et_email.text.toString()
//                                    Toast.makeText(this,"Email already used", Toast.LENGTH_LONG).show()
//                                    et_email.validator()
//                                        .addRule(EmailValid())
//                                        .addErrorCallback { et_email.error =it }
//                                        .check()
//                                }
//                            }
//                        }
//                },com.android.volley.Response.ErrorListener { error ->
//                    Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//                    Log.e("wow", error.message)
//                })
//                rq.add(sr)
//                validasi()
//        }

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
                    Toast.makeText(this@EditProfileAct, error, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ItemProvince>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(this@EditProfileAct, "Message : Error " + t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun popUpCity(etCity: EditText, etProvince: EditText) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val alertLayout = inflater.inflate(R.layout.custom_dialog_search, null)

        alert = androidx.appcompat.app.AlertDialog.Builder(this@EditProfileAct)
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
        adapter_city = CityAdapter(this@EditProfileAct, ListCity)
        mListView!!.isClickable = true

        mListView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val o = mListView!!.getItemAtPosition(i)
            val cn = o as com.example.ta.Model.kota.Result

            etCity.error = null
            etCity.setText(cn.cityName)
            etCity.tag = cn.cityId

            ad?.dismiss()
        }

        progressDialog = ProgressDialog(this@EditProfileAct)
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
                    Toast.makeText(this@EditProfileAct, error, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ItemCity>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(this@EditProfileAct, "Message : Error " + t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this,ProfileAct::class.java))
        }
        toolbar.title = "Edit Profile"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.white_transparency)
        Tools.setSystemBarLight(this)
    }

    @SuppressLint("SetTextI18n")
    fun clickDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in Toast
            Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()
            et_age.setText(year.toString()+"/"+ (monthOfYear+1).toString()+"/"+(dayOfMonth).toString())
            yearU = year

        }, year, month, day)
        dpd.show()
    }

    fun validasi()
    {
//        et_usrname.text.toString(),
//        et_name.text.toString(),
//        et_newpass.text.toString(),
//        et_email.text.toString(),
//        et_phone.text.toString(),
//        etProvince.text.toString(),
//        etCity.text.toString(),
//        et_address.text.toString(),
//        et_age.text.toString(),

       et_usrname.validator()
           .nonEmpty()
           .minLength(5)
           .addErrorCallback { et_usrname.error = it }
           .check()

        et_name.validator()
            .nonEmpty()
            .addErrorCallback { et_name.error = it }
            .check()

        et_newpass.validator()
            .nonEmpty()
            .minLength(6)
//            .atleastOneUpperCase()
            .addErrorCallback { et_newpass.error = it }
            .check()

        et_email.validator()
            .nonEmpty()
            .validEmail()
            .addErrorCallback { et_email.error = it }
            .check()

        et_phone.validator()
            .nonEmpty()
            .onlyNumbers()
            .addErrorCallback { et_phone.error = it }
            .check()

        etToCity.validator()
            .nonEmpty()
            .noNumbers()
            .addErrorCallback { etToCity.error = it }
            .check()

        etToProvince.validator()
            .nonEmpty()
            .noNumbers()
            .addErrorCallback { etToProvince.error = it }
            .check()
    }

}

