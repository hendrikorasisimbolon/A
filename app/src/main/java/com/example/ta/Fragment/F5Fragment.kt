package com.example.ta.Fragment


import android.app.DialogFragment
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.ta.R


class F5Fragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_f5, container, false)

        var intent:Intent = activity.intent
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NO_ANIMATION)
        activity.overridePendingTransition(0,0)
        activity.finish()

        activity.overridePendingTransition(0,0)
        startActivity(intent)
        onStop()
        return  v
    }


}
