package app.isolvetech.mycanva

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

class MyCanvaView(context: Context) : View(context) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private lateinit var frame: Rect

    private val STROKE_WIDTH = 12f
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop



    private var currentX = 0f
    private var currentY = 0f

    private val bgColor = ResourcesCompat.getColor(resources, R.color.purple_200, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.black, null)

    private var path = Path()

    private val drawing  = Path()
    private val currentPath = Path()

    private  val paint = Paint().apply {
        color = drawColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)

        extraCanvas.drawColor(bgColor)

        //create a rectangular frame around the drawing
        val inset = 40
        frame = Rect(inset, inset, w - inset, h - inset)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap,0f, 0f, null)
//        canvas?.drawPath(drawing, paint)
//        canvas?.drawPath(currentPath, paint)
        canvas?.drawRect(frame, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            motionTouchEventX = event.x
            motionTouchEventY = event.y
        }


        when(event?.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        return true
    }

    private fun touchUp() {
        drawing.addPath(currentPath)
        currentPath.reset()
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY

            extraCanvas.drawPath(path, paint)
        }

        invalidate()
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX,motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }
}