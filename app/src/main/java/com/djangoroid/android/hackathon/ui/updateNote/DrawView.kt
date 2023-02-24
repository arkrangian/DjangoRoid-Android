package com.djangoroid.android.hackathon.ui.updateNote

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class DrawView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {

    private var x = 0f
    private var y = 0f
    private var path: Path? = null

    // the Paint class encapsulates the color
    // and style information about
    // how to draw the geometries,text and bitmaps
    private var paint: Paint = Paint()

    // ArrayList to store all the strokes
    // drawn by the user on the Canvas
    private val paths = ArrayList<Stroke>()
    private var currentColor = 0
    private var strokeWidth = 0
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private val bitmapPaint = Paint(Paint.DITHER_FLAG)

    // Constructors to initialise all the attributes
    init {

        // the below methods smoothens
        // the drawings of the user
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND

        // 0xff=255 in decimal
        paint.alpha = 0xff
    }

    // this method instantiate the bitmap and object
    fun init(height: Int, width: Int) {

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)

        // set an initial color of the brush
        currentColor = Color.BLACK

        // set an initial brush size
        strokeWidth = 20
    }

    // sets the current color of stroke
    fun setColor(color: Int) {
        currentColor = color
    }

    // sets the stroke width
    fun setStrokeWidth(width: Int) {
        strokeWidth = width
    }

    fun undo() {
        // check whether the List is empty or not
        // if empty, the remove method will return an error
        if (paths.size != 0) {
            paths.removeAt(paths.size - 1)
            invalidate()
        }
    }

    // this methods returns the current bitmap
    fun save(): Bitmap? {
        return bitmap
    }

    // this is the main method where
    // the actual drawing takes place
    override fun onDraw(canvas: Canvas) {
        // save the current state of the canvas before,
        // to draw the background of the canvas
        canvas.save()

        // DEFAULT color of the canvas
        val backgroundColor = Color.WHITE
        canvas.drawColor(backgroundColor)

        // now, we iterate over the list of paths
        // and draw each path on the canvas
        for (fp in paths) {
            paint.color = fp.color
            paint.strokeWidth = fp.strokeWidth.toFloat()
            canvas.drawPath(fp.path!!, paint)
        }
        canvas.drawBitmap(bitmap!!, 0f, 0f, bitmapPaint)
        canvas.restore()
    }

    // the below methods manages the touch
    // response of the user on the screen
    // firstly, we create a new Stroke
    // and add it to the paths list
    private fun touchStart(valueX: Float, valueY: Float) {
        path = Path()
        val fp = Stroke(currentColor, strokeWidth, path)
        paths.add(fp)

        // finally remove any curve
        // or line from the path
        path!!.reset()

        // this methods sets the starting
        // point of the line being drawn
        path!!.moveTo(valueX, valueY)

        // we save the current
        // coordinates of the finger
        x = valueX
        y = valueY
    }

    // in this method we check
    // if the move of finger on the
    // screen is greater than the
    // Tolerance we have previously defined,
    // then we call the quadTo() method which
    // actually smooths the turns we create,
    // by calculating the mean position between
    // the previous position and current position
    private fun touchMove(valueX: Float, valueY: Float) {
        val dx = Math.abs(valueX - x)
        val dy = Math.abs(valueY - y)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path!!.quadTo(x, y, (valueX + x) / 2, (valueY + y) / 2)
            x = valueX
            y = valueY
        }
    }

    // at the end, we call the lineTo method
    // which simply draws the line until
    // the end position
    private fun touchUp() {
        path!!.lineTo(x, y)
    }

    // the onTouchEvent() method provides us with
    // the information about the type of motion
    // which has been taken place, and according
    // to that we call our desired methods
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }
}