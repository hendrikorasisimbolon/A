package com.example.ta


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.CityAdapter
import com.example.ta.Adapter.ProvinceAdapter
import com.example.ta.Api.ApiServiceRO
import com.example.ta.Api.ApiUrl
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.Model.kota.ItemCity
import com.example.ta.Model.provinsi.ItemProvince
import com.example.ta.Model.provinsi.Result
import com.example.ta.RulesExtra.EmailValid
import com.example.ta.RulesExtra.NameValid
import com.example.ta.RulesExtra.UsernameValid
import com.google.gson.Gson
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import androidx.appcompat.app.AlertDialog as AlertDialog1

class RegisterAct : AppCompatActivity() {

    companion object{
        var username_ =""
        var name_=""
        var email_ =""
    }

    private var alert: androidx.appcompat.app.AlertDialog.Builder? = null
    private var ad: androidx.appcompat.app.AlertDialog? = null
    private var searchList: EditText? = null
    private var mListView: ListView? = null
    private val ListProvince = ArrayList<Result>()
    private var adapter_province: ProvinceAdapter? = null
    private var progressDialog: ProgressDialog? = null
    private var adapter_city: CityAdapter? = null
    var lahir:String = ""
    private val ListCity = ArrayList<com.example.ta.Model.kota.Result>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etToProvince.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                (View.OnClickListener { popUpProvince(etToProvince, etToCity) })
            }
        }

        etToCity.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                try {
                    if (etToProvince.tag == "") {
                        etToProvince.error = "Please chooice your to province"
                    } else {
                        popUpCity(etToCity, etToProvince)
                    }

                } catch (e: NullPointerException) {
                    etToProvince.error = "Please chooice your to province"
                }
            }

        }
        umurEditText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
            {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in Toast
                    Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()
                    lahir = year.toString()+"/"+ (monthOfYear+1).toString()+"/"+(dayOfMonth).toString()
                    umurEditText.setText(lahir)
                }, year, month, day)
                dpd.show()
            }
        }

        btn_login.setOnClickListener{
            var i = Intent(this,LoginAct::class.java)
            startActivity(i)
        }
        btn_signUp.setOnClickListener{
            if(!nameEditText.text.isNullOrBlank() && !usernameEditText.text.isNullOrBlank() && !emailEditText.text.isNullOrBlank() &&
                !passwordEditText.text.isNullOrBlank() && !confrpasswordEditText.text.isNullOrBlank() && !phoneEditText.text.isNullOrBlank() && !addressEditText.text.isNullOrBlank() && !umurEditText.text.isNullOrBlank() && !etToCity.text.isNullOrBlank() && !etToProvince.text.isNullOrBlank())
            {
                if(passwordEditText.text.toString().equals(confrpasswordEditText.text.toString()))
                {
                    var url = url_website + "/udemy/register.php?name=" + nameEditText.text.toString() +"&username=" + usernameEditText.text.toString() + "&email=" + emailEditText.text.toString() + "&password=" + passwordEditText.text.toString() + "&phone=" + phoneEditText.text.toString() + "&address=" + addressEditText.text.toString() + "&age=0" +"&lahir=" + umurEditText.text.toString() + "&provinsi=" + etToProvince.tag + "&kota=" + etToCity.tag
                    var rq:RequestQueue = Volley.newRequestQueue(this)
                    var sr=StringRequest(Request.Method.GET,url,com.android.volley.Response.Listener { response ->
                        if (response.equals("0")){
                            Toast.makeText(this,"Register not success", Toast.LENGTH_LONG).show()
                        }
                        if (response.equals("sukses")){
                            var i = Intent(this, LoginAct::class.java)
                            startActivity(i)
                        }
                        else {
//                        Toast.makeText(this,"User already used", Toast.LENGTH_LONG).show()
                            var part: List<String> = response.split(" ")
                            for (i in 0 .. part.size-1){
                                if (part[i]=="-1")
                                {
                                    username_ = usernameEditText.text.toString()
                                    Toast.makeText(this,"Username already used", Toast.LENGTH_LONG).show()
                                    usernameEditText.validator()
                                        .addRule(UsernameValid())
                                        .addErrorCallback { usernameEditText.error = it }
                                        .check()
                                }
                                if (part[i]=="-2"){
                                    name_ = nameEditText.text.toString()
                                    Toast.makeText(this,"Name already used", Toast.LENGTH_LONG).show()
                                    nameEditText.validator()
                                        .addRule(NameValid())
                                        .addErrorCallback { nameEditText.error = it }
                                        .check()
                                }
                                if(part[i]=="-3"){
                                    email_ = emailEditText.text.toString()
                                    Toast.makeText(this,"Email already used", Toast.LENGTH_LONG).show()
                                    emailEditText.validator()
                                        .addRule(EmailValid())
                                        .addErrorCallback { emailEditText.error =it }
                                        .check()
                                }
                            }
                        }

                    }, com.android.volley.Response.ErrorListener { error ->
                        Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
                    })
                    rq.add(sr)
                }
                else
                {
                    Toast.makeText(this,"Password not match", Toast.LENGTH_LONG).show()
                }
            }
            validasi()
        }


    }

    fun popUpProvince(etProvince: EditText, etCity: EditText) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val alertLayout = inflater.inflate(R.layout.custom_dialog_search, null)

        alert = androidx.appcompat.app.AlertDialog.Builder(this@RegisterAct)
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
        adapter_province = ProvinceAdapter(this@RegisterAct, ListProvince)
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
                    Toast.makeText(this@RegisterAct, error, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ItemProvince>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(this@RegisterAct, "Message : Error " + t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun popUpCity(etCity: EditText, etProvince: EditText) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val alertLayout = inflater.inflate(R.layout.custom_dialog_search, null)

        alert = androidx.appcompat.app.AlertDialog.Builder(this@RegisterAct)
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
        adapter_city = CityAdapter(this@RegisterAct, ListCity)
        mListView!!.isClickable = true

        mListView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val o = mListView!!.getItemAtPosition(i)
            val cn = o as com.example.ta.Model.kota.Result

            etCity.error = null
            etCity.setText(cn.cityName)
            etCity.tag = cn.cityId

            ad?.dismiss()
        }

        progressDialog = ProgressDialog(this@RegisterAct)
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
                    Toast.makeText(this@RegisterAct, error, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ItemCity>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(this@RegisterAct, "Message : Error " + t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
//
//    fun clickDataPicker(view: View) {
//
//    }

    private fun validasi()
    {
        nameEditText.validator()
            .nonEmpty()
            .addErrorCallback { nameEditText.error = it }
            .check()

        usernameEditText.validator()
            .nonEmpty()
            .minLength(6)
            .addErrorCallback { usernameEditText.error = it }
            .check()

        emailEditText.validator()
            .nonEmpty()
            .validEmail()
            .addErrorCallback { emailEditText.error = it }
            .check()

        passwordEditText.validator()
            .nonEmpty()
            .minLength(6)
//            .atleastOneUpperCase()
            .addErrorCallback { passwordEditText.error = it }
            .check()

        confrpasswordEditText.validator()
            .nonEmpty()
            .minLength(6)
//            .atleastOneUpperCase()
            .addErrorCallback { confrpasswordEditText.error = it }
            .check()

        phoneEditText.validator()
            .nonEmpty()
            .onlyNumbers()
            .addErrorCallback { phoneEditText.error = it }
            .check()

        addressEditText.validator()
            .nonEmpty()
            .addErrorCallback { addressEditText.error = it }
            .check()

        umurEditText.validator()
            .nonEmpty()
            .addErrorCallback { umurEditText.error = it }
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


