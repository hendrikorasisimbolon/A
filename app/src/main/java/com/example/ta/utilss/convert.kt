package com.example.ta.utilss

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import com.pchmn.materialchips.R2.attr.layoutManager
import androidx.recyclerview.widget.RecyclerView


data class page(
    var berat: String,
    var deskripsi: String,
    var diskon: String,
    var foto: String,
    var foto_type: String,
    var harga_diskon: String,
    var harga_normal: String,
    var id_produk: String,
    var judul_kategori: String,
    var judul_produk: String,
    var judul_subkategori: String,
    var kat_id: String,
    var stok: String,
    var subkat_id: String
)