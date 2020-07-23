package com.nixsolutions.customclock.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatSeekBar
import com.nixsolutions.customclock.ui.data.ClockTime

class TimeZoneView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.seekBarStyle
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    private var isOrientationHorizontal = true
    private val clockTime = ClockTime()
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        textSize = 40f
    }

    init {
        max = TIMEZONE_COUNT
        progress = DEFAULT_TIMEZONE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTimezonesValue(canvas)
    }

    fun horizontalOrientation() {
        isOrientationHorizontal = true
        rotation = HORIZONTAL_ROTATION
    }

    fun verticalOrientation() {
        isOrientationHorizontal = false
        rotation = VERTICAL_ROTATION
    }

    private fun drawTimezonesValue(canvas: Canvas?) {
        for (timezoneNumber in 0..TIMEZONE_COUNT) {
            paint.color = Color.BLACK

            if (timezoneNumber == progress) {
                paint.color = Color.RED
            }

            if (isOrientationHorizontal) {
                drawHorizontal(canvas, timezoneNumber)
            } else {
                drawVertical(canvas, timezoneNumber)
            }
        }
    }

    private fun drawHorizontal(canvas: Canvas?, timezoneNumber: Int) {
        val sizeX = (width.toFloat() / TIMEZONE_COUNT) * 0.85f
        canvas?.drawText(
            clockTime.getTimeZone(timezoneNumber + 1).second,
            (timezoneNumber * sizeX) + 40,
            calculateY(timezoneNumber, sizeX),
            paint
        )
    }

    private fun drawVertical(canvas: Canvas?, timezoneNumber: Int) {
        val sizeY = (width.toFloat() / TIMEZONE_COUNT) * 0.9f
        canvas?.translate(canvas.clipBounds.bottom.toFloat(), ZERO_COORDINATE)
        canvas?.rotate(RIGHT_ANGEL)
        canvas?.drawText(
            clockTime.getTimeZone(timezoneNumber + 1).second,
            calculateX(timezoneNumber, sizeY),
            width.toFloat() * 0.88f - ((timezoneNumber - 1) * sizeY),
            paint
        )
        canvas?.translate(ZERO_COORDINATE, canvas.clipBounds.right.toFloat())
        canvas?.rotate(-RIGHT_ANGEL)
    }

    private fun calculateY(timezoneNumber: Int, size: Float) =
        if (timezoneNumber % 2 == 0) {
            height / 2f - size
        } else {
            height / 2f + size
        }

    private fun calculateX(timezoneNumber: Int, size: Float) =
        if (timezoneNumber % 2 == 0) {
            (width / 2f) - (size * 2.5f)
        } else {
            (width / 2f) + (size * 0.5f)
        }

    companion object {

        private const val TIMEZONE_COUNT = 14
        private const val DEFAULT_TIMEZONE = 8
        private const val RIGHT_ANGEL = 90f
        private const val ZERO_COORDINATE = 0f
        private const val HORIZONTAL_ROTATION = 0f
        private const val VERTICAL_ROTATION = 270f
    }
}