package com.nixsolutions.customclock.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import com.nixsolutions.customclock.R
import kotlinx.android.synthetic.main.view_custom_time.view.*

class TimeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        orientation = if (widthMeasureSpec > heightMeasureSpec) {
            timeZoneView.verticalOrientation()
            HORIZONTAL
        } else {
            timeZoneView.horizontalOrientation()
            VERTICAL
        }

        clockView.setTimezone(timeZoneView.progress)
    }

    init {
        initializeViews(context)
        orientation = VERTICAL
        timeZoneView.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                // empty
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) =
                clockView.setTimezone(timeZoneView.progress)
        })
    }

    private fun initializeViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_custom_time, this)
    }
}