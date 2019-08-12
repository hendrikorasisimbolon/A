package com.example.ta

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Fragment.UpdateAddressFragment
import com.example.ta.Model.MCart
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley
import com.example.ta.Model.UserInfo
import com.example.ta.utilss.Tools
import com.example.ta.utilss.UserSessionManager
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_komplain.*
import kotlinx.android.synthetic.main.toolbar.*
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import java.io.IOException
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class KomplainAct : AppCompatActivity() {
    data class Temp(
        var id_trandet:String,
        var judul:String,
        var id_pro:String
    )
    companion object{
        var id_trans = ""
    }
    var trans = ""
    var status = ""
    var produk = ArrayList<Temp>()
    private val IMAGE_REQUEST_CODE = 3
    private var bitmap: Bitmap? = null
    private val STORAGE_PERMISSION_CODE = 123
    private  var uri: Uri?= null
    val UPLOAD_URL = Url_Volley.url_website +"/ecommerce/upload2.php"
    lateinit var user: UserInfo
    lateinit var session: UserSessionManager

    private lateinit var ui_hot:TextView
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_komplain)
        session = UserSessionManager(applicationContext)
        initToolbar()
        user = session.userDetails

        trans =  intent.getStringExtra("id_trans").toString()
        et_transaksi.setText("ID transaksi : "+ trans)

        et_rek.validator()
            .nonEmpty()
            .minLength(8)
            .addErrorCallback { et_rek.error = it }
            .check()
        txt_alasan.validator()
            .nonEmpty()
            .minLength(8)
            .addErrorCallback { txt_alasan.error = it }
            .check()

        img.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Choose image..."), IMAGE_REQUEST_CODE)
        }
        //Dana 0, Barang 1
        rdo_dana.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                status = "0"
                alamat_komplain.visibility = View.GONE
                et_rek.visibility = View.VISIBLE
                Log.e("STATUSSSS", status)

            }
        }

        rdo_barang.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                status = "1"
                alamat_komplain.visibility = View.VISIBLE
                et_rek.visibility = View.GONE
                Log.e("STATUSSSS", status)
            }
        }

        btn_edit_alamat.setOnClickListener {
            var obj = UpdateAddressFragment()
            var mann = this.fragmentManager
            obj.show(mann, "Srv")
        }

        txt_alamat_komplain.text =
            user.address + ", " + user.nama_kota + System.getProperty("line.separator") + user.nama_provinsi + System.getProperty("line.separator") + "(+62) "+ user.phone

        requestStoragePermission()
        btn_kirim.setOnClickListener{
            if(rdo_barang.nonEmpty() || rdo_dana.nonEmpty()) {
                var url =
                    Url_Volley.url_website + "/udemy/komplain.php?user_id=" + MCart.user_id + "&trans_id=" + trans + "&norek=" + et_rek.text.toString() + "&alasan=" + txt_alasan.text.toString()
                var rq: RequestQueue = Volley.newRequestQueue(this)
                var jar = StringRequest(Request.Method.GET, url, Response.Listener { response ->

                }, Response.ErrorListener { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                })
                rq.add(jar)
                if ((et_rek.nonEmpty() && status=="1") || txt_alasan.nonEmpty()) {
                    uploadMultipart()

                }
            }
            else
            {
                Toast.makeText(this, "Silahkan pilih cara pengembalian!", Toast.LENGTH_LONG).show()
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)
                img.setImageBitmap(bitmap)
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
                .addParameter("user", MCart.user_id+","+trans+","+status) //Adding text parameter to the request
//                .addParameter("trans_id", trans) //Adding text parameter to the request
                .setNotificationConfig(UploadNotificationConfig())
                .setMaxRetries(2)
                .startUpload() //Starting the upload
        } catch (exc: Exception) {
            Toast.makeText(this, exc.message, Toast.LENGTH_SHORT).show()
        }

    }


    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.getNavigationIcon()?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
        toolbar.title = "Komplain"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.white_transparency)
        Tools.setSystemBarLight(this)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart_setting, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
        Tools.changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.indigo_500))
        val menuItem: MenuItem = menu.findItem(R.id.aksi_cart)
        var actionView: View = MenuItemCompat.getActionView(menuItem)
        ui_hot = actionView.findViewById(R.id.hotlist_hot) as TextView
//        var i = Intent(this, OrderAct::class.java)
//        menuItem.intent = i
        setupBadge()

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.aksi_cart)
        {
            var i = Intent(this, OrderAct::class.java)
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupBadge() {

        if (ui_hot != null) {
            if (MTotalCart.total_cart.toInt() == 0) {
                if (ui_hot.getVisibility() !== View.GONE) {
                    ui_hot.setVisibility(View.GONE)
                }
            } else {
//                ui_hot.setText(String.valueOf(Math.min(mCartItemCount, 99)))
                ui_hot.setText(Math.min(MTotalCart.total_cart.toInt(),99).toString())
                if (ui_hot.getVisibility() !== View.VISIBLE) {
                    ui_hot.setVisibility(View.VISIBLE)
                }
            }
        }
    }
}
