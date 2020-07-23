package com.nixsolutions.customclock.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.nixsolutions.customclock.R
import com.nixsolutions.customclock.ui.data.ClockTime
import kotlin.math.cos
import kotlin.math.sin

class ClockView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val faceColor = resources.getColor(R.color.clockBackground)
    private val borderColor = resources.getColor(R.color.clockBorders)
    private val handColor = resources.getColor(R.color.clockHand)
    private val secondsHandColor = resources.getColor(R.color.clockSecondsHand)
    private val paint = Paint()
    private val clockTime = ClockTime()
    private var size = DEFAULT_VIEW_SIZE
    private var radius = size / 2f
    private var timeZone = DEFAULT_TIMEZONE
    private var textSize = DEFAULT_TEXT_SIZE
    private var isViewActive: Boolean = true
    private lateinit var cachedBitmap: Bitmap

    init {
        getAttrs(attrs)
        clockTime.initializeDate(timeZone)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (clockTime.isInitialDrawing) {
            val cacheCanvas = Canvas(cachedBitmap)
            drawClockBody(cacheCanvas)
            drawClockBorders(cacheCanvas)
            clockTime.isInitialDrawing = false
        }

        drawCachedBodyAndHands(canvas)
        clockTime.updateDate()
        if (isViewActive) {
            postInvalidateDelayed(DELAY_MILLISECONDS)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = measuredWidth.coerceAtMost(measuredHeight)
        radius = size / 2f
        setMeasuredDimension(size, size)
        cachedBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        clockTime.reset()
    }

    fun setTimezone(timezoneValue: Int) {
        clockTime.apply { initializeDate(this.getTimeZone(timezoneValue + 1).first) }
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ClockView, 0, 0)
        timeZone = clockTime.getTimeZone(
            typedArray.getInt(R.styleable.ClockView_timeZone, DEFAULT_TIMEZONE_KEY)
        ).first
        textSize = typedArray.getDimension(R.styleable.ClockView_textSize, DEFAULT_TEXT_SIZE)
    }

    private fun drawCachedBodyAndHands(canvas: Canvas) {
        canvas.drawBitmap(cachedBitmap, ZERO_COORDINATE, ZERO_COORDINATE, paint)
        drawSecondArrow(canvas)
        drawMinuteArrow(canvas)
        drawHourArrow(canvas)
    }

    private fun drawClockBody(canvas: Canvas) {
        paint.color = faceColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(radius, radius, radius, paint)

        paint.color = borderColor
        canvas.drawCircle(radius, radius, radius / 40f, paint)
    }

    private fun drawClockBorders(canvas: Canvas) {
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = BORDER_WIDTH
        canvas.drawCircle(radius, radius, radius - BORDER_WIDTH / 2f, paint)
    }

    private fun drawSecondArrow(canvas: Canvas) {
        paint.color = secondsHandColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = SECOND_HAND_WIDTH

        val angel = Math.toRadians(90 - (6 * clockTime.seconds))
        drawArrow(canvas, angel, 0.7f)
    }

    private fun drawMinuteArrow(canvas: Canvas) {
        paint.color = handColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = MINUTE_HAND_WIDTH

        val angel = Math.toRadians(90 - (6 * (clockTime.minutes + (clockTime.seconds / 60.0))))
        drawArrow(canvas, angel, 0.75f)
    }

    private fun drawHourArrow(canvas: Canvas) {
        paint.color = handColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = HOUR_HAND_WIDTH

        val angel = Math.toRadians(90 - (30 * (clockTime.hours + (clockTime.minutes / 60.0))))
        drawArrow(canvas, angel, 0.5f)
    }

    private fun drawArrow(canvas: Canvas, angel: Double, sizeCoefficient: Float) {
        val stopX = radius + (radius * sizeCoefficient * cos(angel).toFloat())
        val stopY = radius - (radius * sizeCoefficient * sin(angel).toFloat())

        canvas.drawLine(
            radius,
            radius,
            stopX,
            stopY,
            paint
        )
    }

    companion object {

        private const val DEFAULT_TIMEZONE = "GMT+03:00"
        private const val DEFAULT_TEXT_SIZE = 50f
        private const val DEFAULT_TIMEZONE_KEY = 9
        private const val DEFAULT_VIEW_SIZE = 320
        private const val BORDER_WIDTH = 24.0f
        private const val HOUR_HAND_WIDTH = 15.0f
        private const val MINUTE_HAND_WIDTH = 10.0f
        private const val SECOND_HAND_WIDTH = 8.0f
        private const val DELAY_MILLISECONDS = 1000L
        private const val ZERO_COORDINATE = 0f
    }
}