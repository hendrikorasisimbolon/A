package com.example.ta.customfonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView


class MyTextView : TextView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        if (!isInEditMode) {
            val tf = Typeface.createFromAsset(context.assets, "fonts/MavenPro-Regular.ttf")
            typeface = tf
        }
    }

}