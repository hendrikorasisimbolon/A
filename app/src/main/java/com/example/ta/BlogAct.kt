package com.example.ta

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.BlogAdapter
import com.example.ta.Adapter.OnItemClickListener
import com.example.ta.Adapter.addOnItemClickListener
import com.example.ta.Model.MBlog
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.utilss.Tools

import kotlinx.android.synthetic.main.activity_blog.*
import kotlinx.android.synthetic.main.activity_dashboard_grid_fab.*
import kotlin.collections.ArrayList

class BlogAct : AppCompatActivity() {

    var list = ArrayList<MBlog>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)
        initToolbar()

        var url = url_website + "/udemy/get_blog.php"
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
            for(x in 0..response.length()-1)
            {
                list.add(
                    MBlog(
                        response.getJSONObject(x).getInt("id_blog"),
                        response.getJSONObject(x).getString("judul_blog"),
                        response.getJSONObject(x).getString("isi_blog"),
                        response.getJSONObject(x).getString("created"),
                        response.getJSONObject(x).getString("modified_by"),
                        response.getJSONObject(x).getString("foto"),
                        response.getJSONObject(x).getString("foto_type")
                    )
                )
            }

            var adp= BlogAdapter(this,list)
            blog_rv.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(this)
//            item_rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            blog_rv.adapter=adp


        },Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()

        })
        rq.add(jar)

        blog_rv.addOnItemClickListener(object : OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                var i = Intent(this@BlogAct, BlogDetailAct::class.java)
                i.putExtra("judul_blog", list.get(position).judul_blog)
                i.putExtra("isi_blog", list.get(position).isi_blog)
                i.putExtra("pengarang_blog",list.get(position).modified_by)
                i.putExtra("foto_blog",list.get(position).foto)
                i.putExtra("foto_type_blog",list.get(position).foto_type)
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i)
//                Toast.makeText(this@BlogAct,"Klik ke "+position.toString(),Toast.LENGTH_LONG).show()
            }

        })


    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Artikel"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_10)
        Tools.setSystemBarLight(this)
        toolbar.setNavigationOnClickListener{
            onBackPressed()

        }
    }

}
