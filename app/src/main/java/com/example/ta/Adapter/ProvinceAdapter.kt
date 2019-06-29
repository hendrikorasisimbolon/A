package com.example.ta.Adapter

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ta.Model.provinsi.Result
import com.example.ta.R

import java.util.ArrayList

/**
 * Created by Robby Dianputra on 1/5/2018.
 */

class ProvinceAdapter
/**
 * Instantiates a new Inbox adapter.
 *
 * @param activity   the activity
 * @param movieItems the movie items
 */
    (private val activity: AppCompatActivity, private val movieItems: MutableList<Result>) : BaseAdapter() {
    private var inflater: LayoutInflater? = null
    private val listlokasiasli: ArrayList<Result>


    init {

        listlokasiasli = ArrayList()
        listlokasiasli.addAll(movieItems)
    }

    override fun getCount(): Int {
        return movieItems.size
    }

    override fun getItem(location: Int): Any {
        return movieItems[location]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (inflater == null)
            inflater = activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null)
            convertView = inflater!!.inflate(R.layout.item_category, null)

        val tv_category = convertView!!.findViewById<View>(R.id.tv_category) as TextView
        val tv_detail = convertView.findViewById<View>(R.id.tv_detail) as TextView

        val m = movieItems[position]

        tv_category.text = m.province
        tv_detail.text = m.provinceId

        return convertView
    }

    @SuppressLint("DefaultLocale")
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase()

        movieItems.clear()
        if (charText.length == 0) {
            /* tampilkan seluruh data */
            movieItems.addAll(listlokasiasli)

        } else {
            for (lok in listlokasiasli) {
                if (lok.province?.toLowerCase()?.contains(charText)!!) {
                    movieItems.add(lok)
                } else {
                }

            }
        }

        notifyDataSetChanged()
    }

    fun setList(movieItems: List<Result>) {
        this.listlokasiasli.addAll(movieItems)
    }
}
