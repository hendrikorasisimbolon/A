//package com.example.ta.Adapter
//
//
//import android.content.Context
//import android.widget.TextView
//import com.smarteist.autoimageslider.SliderViewAdapter
//import com.bumptech.glide.Glide
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import com.example.ta.Model.SliderInfo
//import com.example.ta.R
//import com.squareup.picasso.Picasso
//
//
//public class SliderAdapter ( val context: Context, var list:ArrayList<SliderInfo>) : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
//    override fun getCount(): Int {
//        return list.size
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
//        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
//        return SliderAdapterVH(inflate)
//    }
//
//    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
//        viewHolder.textViewDescription.text = list[position].title
//
//        for (x in 0 .. list.size-1)
//        {
//            if (x == position)
//            {
//                Picasso.with(context)
//                    .load("http://192.168.43.180/ecommerce/assets/images/slider/"+list[position].site)
//                    .into(viewHolder.imageViewBackground)
//            }
//        }
//
////        when (position) {
////            0 -> Glide.with(viewHolder.itemView)
////                .load("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
////                .into(viewHolder.imageViewBackground)
////            1 -> Glide.with(viewHolder.itemView)
////                .load("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260")
////                .into(viewHolder.imageViewBackground)
////            2 -> Glide.with(viewHolder.itemView)
////                .load("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
////                .into(viewHolder.imageViewBackground)
////            else -> Glide.with(viewHolder.itemView)
////                .load("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
////                .into(viewHolder.imageViewBackground)
////        }
//
//    }
//
//    inner class SliderAdapterVH(var itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
//        var imageViewBackground: ImageView
//        var textViewDescription: TextView
//
//        init {
//            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
//            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider)
//        }
//    }
//}