package com.example.ta.Fragment


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import com.example.ta.Adapter.SearchAdapter
import com.example.ta.Model.MItemDetail

import com.example.ta.R
import com.example.ta.utilss.Tools

class NotFoundFragment : Fragment() {

    var tampung:ArrayList<MItemDetail> = ArrayList()
    var dat = MItemDetail.data
    var adapter: SearchAdapter? = null
    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v =  inflater.inflate(R.layout.fragment_error, container, false)
        setHasOptionsMenu(true)
        return v
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem!!.getActionView() as SearchView
            searchView!!.queryHint = "Cari"
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    var nx = SearchFragment()
                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.halaman, nx)
                        .commit()
                    adapter!!.notifyDataSetChanged()
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
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
