package com.example.ta.Fragment


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ta.Adapter.SearchAdapter
import com.example.ta.Model.MItemDetail
import com.example.ta.ProductDetailAct
import com.example.ta.R
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.toolbar.*


class SearchFragment : Fragment(), SearchAdapter.OnNoteListener {


    var tampung:ArrayList<MItemDetail> = ArrayList()
    var dat = MItemDetail.data
    var adapter: SearchAdapter? = null
    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null
    var toolbar: Toolbar?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_search, container, false)
        Log.e("banyakData1",dat.size.toString())
        for(i in 0..dat.size-1){
            Log.e("dataX", dat[i].judul_produk)
        }
        setHasOptionsMenu(true)
        toolbar = activity!!.toolbar
        activity!!.setTitle("Search")
        activity!!.toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)

        var r = v.findViewById(R.id.recycler) as RecyclerView
//        tampung = MItemDetail.data
        var adp=SearchAdapter(activity!!,tampung,this)
        r.layoutManager= LinearLayoutManager(activity)
        r.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        r.adapter=adp
        adp.notifyDataSetChanged()
//        activity!!.toolbar.setNavigationOnClickListener {
//            activity!!.finish()
//        }
        return v
    }


    override fun onNoteClick(position: Int) {
        var intent = Intent(activity, ProductDetailAct::class.java)
        intent.putExtra("id_produk", tampung.get(position).id.toString())
        intent.putExtra("judul_produk", tampung.get(position).judul_produk)
        intent.putExtra("harga_normal", tampung.get(position).harga_normal.toString())
        intent.putExtra("harga_diskon", tampung.get(position).harga_diskon.toString())
        intent.putExtra("foto", tampung.get(position).foto)
        intent.putExtra("stok", tampung.get(position).stok)
        intent.putExtra("foto_type", tampung.get(position).foto_type)
        intent.putExtra("berat", tampung.get(position).berat.toString())
        intent.putExtra("deskripsi", tampung.get(position).deskripsi)
        intent.putExtra("discount",tampung.get(position).diskon.toString())
        intent.putExtra("harga_normal",tampung.get(position).harga_normal.toString())

        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchItem.expandActionView()
        if (searchItem != null) {
            searchView = searchItem!!.getActionView() as SearchView
            searchView!!.queryHint = "Cari"
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

            queryTextListener = object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    tampung.clear()
//                    onStart()
                    for (i in 0..dat.size-1) {
                        Log.e("dariCari", dat[i].judul_produk.toString())
                        val nama = dat[i].judul_produk.toLowerCase()
                        if (nama.contains(newText.toLowerCase())) {
                            tampung.add(dat[i])
                        }
                    }
                    return false
                }


                override fun onQueryTextSubmit(query: String): Boolean {
//                    tampung.clear()
                    if (tampung.size == 0)
                    {
                        var nx = NotFoundFragment()
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.halaman, nx)
                            .commit()
                    }
//                    for (i in 0..dat.size-1) {
//                        Log.e("dariCari", dat[i].judul_produk.toString())
//                        val nama = dat[i].judul_produk.toLowerCase()
//                        if (nama.contains(query.toLowerCase())) {
//                            tampung.add(dat[i])
//                        }
//                    }
////                    adapter?.setFilter(tampung)
//                    tampung.shuffle()
//                    if (tampung.size == 0)
//                    {
//                        var nx = NotFoundFragment()
//                        activity!!.supportFragmentManager.beginTransaction()
//                            .replace(R.id.halaman, nx)
//                            .commit()
//                    }
                    onStop()
                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        else {
            tampung = ArrayList()
            adapter!!.notifyDataSetChanged()
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_search ->
                // Not implemented here
                return false
            else -> {
            }
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }

}
