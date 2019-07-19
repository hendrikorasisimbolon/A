package com.example.ta

//import com.example.ta.utils.Tools

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.CityAdapter
import com.example.ta.Adapter.ProvinceAdapter
import com.example.ta.Api.ApiServiceRO
import com.example.ta.Api.ApiUrl
import com.example.ta.Api.UploadAPI
import com.example.ta.Api.UploaderClient
import com.example.ta.Fragment.F5Fragment
import com.example.ta.Model.MCart
import com.example.ta.Model.ServerResponse
import com.example.ta.Model.Url_Volley
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.Model.UserInfo
import com.example.ta.Model.kota.ItemCity
import com.example.ta.Model.provinsi.ItemProvince
import com.example.ta.Model.provinsi.Result
import com.example.ta.RulesExtra.EmailValid
import com.example.ta.RulesExtra.NameValid
import com.example.ta.RulesExtra.UsernameValid
import com.example.ta.utilss.Tools
import com.example.ta.utilss.UserSessionManager
import com.google.gson.Gson

import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_edit_profile.*
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.*

@Suppress("IMPLICIT_CAST_TO_ANY", "DEPRECATED_IDENTITY_EQUALS")
class EditProfileAct : AppCompatActivity(){


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

    val UPLOAD_URL = url_website+"/ecommerce/upload.php"
    private var bitmap: Bitmap? = null
    private val IMAGE_REQUEST_CODE = 3
    private val STORAGE_PERMISSION_CODE = 123
    private  var uri: Uri?= null
    private var caption = ""


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
        et_oldpass.setText(MCart.password)

        etToProvince.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                popUpProvince(etToProvince, etToCity)
            }
        }

        etToCity.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
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

        et_age.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in Toast
                    Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()
                    et_age.setText(year.toString()+"/"+ (monthOfYear+1).toString()+"/"+(dayOfMonth).toString())
                    yearU = year

                }, year, month, day).show()
            }


        }


        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        var lahir = year - yearU

        image.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Choose image..."), IMAGE_REQUEST_CODE)
        }
        requestStoragePermission()
        btnUpload.setOnClickListener{
            uploadMultipart()
            getImg()
        }

        btn_save.setOnClickListener{
            var urll = url_website+"/udemy/update_user.php?id="+ user.id+
                    "&name="+ et_name.text.toString() +"&username="+ et_usrname.text.toString() +"&email="+ et_email.text.toString()+
                    "&password="+ et_newpass.text.toString() + "&phone="+ et_phone.text.toString() +"&address="+ et_address.text +
                    "&lahir="+ et_age.text.toString() +"&provinsi="+ etToProvince.tag +"&kota="+ etToCity.tag
            var rq: RequestQueue = Volley.newRequestQueue(this)
            var sr = StringRequest(com.android.volley.Request.Method.GET,urll,com.android.volley.Response.Listener { response ->
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
                                user.photo,  //ubah ntar
                                user.photo_type,
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image.setImageBitmap(bitmap);
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    fun getPath(uri: Uri): String {
        var cursor = getContentResolver().query(uri, null, null, null, null)
        cursor.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()

        cursor = getContentResolver().query(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            MediaStore.Images.Media._ID + " = ? ",
            arrayOf<String>(document_id),
            null
        )
        cursor.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()

        return path
    }

    fun uploadMultipart() {
        //getting the actual path of the image
        val path = getPath(uri!!)
        //Uploading code
        try {
            val uploadId = UUID.randomUUID().toString()

            //Creating a multi part request
            MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                .addFileToUpload(path, "image") //Adding file
                .addParameter("username", user.username) //Adding text parameter to the request
                .setNotificationConfig(UploadNotificationConfig())
                .setMaxRetries(2)
                .startUpload() //Starting the upload
        } catch (exc: Exception) {
            Toast.makeText(this, exc.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) === PackageManager.PERMISSION_GRANTED
        )
            return

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    fun getImg() {
        var url = Url_Volley.url_website + "/udemy/get_images.php?id=" + user.id.toString()
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET, url, null, com.android.volley.Response.Listener { response ->
            Log.e("inifoto",response.getJSONObject(0).getString("photo"))
            session.createUserLoginSession(
                UserInfo(
                    user.id,
                    user.name,
                    user.username,
                    user.password,
                    user.email,
                    user.phone,
                    user.nama_provinsi,
                    user.id_provinsi,
                    user.nama_kota,
                    user.id_kota,
                    user.address,
                    user.umur,
                    user.lahir,
                    response.getJSONObject(0).getString("photo"),
                    response.getJSONObject(0).getString("photo_type"),
                    user.created_on
                )
            )
            Log.e("inifoto",user.photo)

        }, com.android.volley.Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            Log.e("inifoto",error.message)

        })
        rq.add(jar)
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show()
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show()
            }
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
        setSupportActionBar(toolbar)
        supportActionBar?.title="Edit Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_10)
        Tools.setSystemBarLight(this)
        toolbar.setNavigationOnClickListener{
            onBackPressed()

        }
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


