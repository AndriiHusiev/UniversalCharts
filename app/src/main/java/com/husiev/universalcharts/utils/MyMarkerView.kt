package com.husiev.universalcharts.utils

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.husiev.universalcharts.R

class MyMarkerView: MarkerView {
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, defStyleAttr: Int) : super(context, defStyleAttr)

    private var tvContent: TextView? = null
    private var mOffset: MPPointF? = null

    init {
        // find layout components
        tvContent = findViewById(R.id.tvContent)
    }

    // callbacks everytime the MarkerView is redrawn,
    // can be used to update the content (user-interface)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        tvContent?.text = "${e?.y}"
    }

    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF(-(width.toFloat() / 2), (-height).toFloat())
        }
        return mOffset as MPPointF
    }
}