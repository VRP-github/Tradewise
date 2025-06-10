package com.trade.tradewise

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

import kotlin.math.roundToInt

class LineChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    var dataPoints: List<Float> = emptyList()
    var labels: List<String> = emptyList()

    private val linePaint = Paint().apply {
        color = Color.parseColor("#6200EE")
        strokeWidth = 6f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val fillPaint = Paint().apply {
        color = Color.parseColor("#996200EE")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val circlePaint = Paint().apply {
        color = Color.parseColor("#6200EE")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val labelPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val yLabelPaint = Paint().apply {
        color = Color.DKGRAY
        textSize = 26f
        textAlign = Paint.Align.RIGHT
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dataPoints.isEmpty()) return

        val paddingBottom = 60f
        val paddingLeft = 100f
        val chartWidth = width - paddingLeft
        val chartHeight = height - paddingBottom

        val spacing = chartWidth / (dataPoints.size - 1)

        val maxValue = dataPoints.maxOrNull() ?: 0f
        val minValue = dataPoints.minOrNull() ?: 0f
        val range = (maxValue - minValue).takeIf { it != 0f } ?: 1f

        val path = Path()
        val fillPath = Path()

        // Start path
        val startY = chartHeight - (dataPoints[0] - minValue) / range * chartHeight
        path.moveTo(paddingLeft, startY)
        fillPath.moveTo(paddingLeft, chartHeight)
        fillPath.lineTo(paddingLeft, startY)

        for (i in 1 until dataPoints.size) {
            val x = paddingLeft + spacing * i
            val y = chartHeight - (dataPoints[i] - minValue) / range * chartHeight
            path.lineTo(x, y)
            fillPath.lineTo(x, y)
        }

        fillPath.lineTo(paddingLeft + spacing * (dataPoints.size - 1), chartHeight)
        fillPath.close()

        canvas.drawPath(fillPath, fillPaint)
        canvas.drawPath(path, linePaint)

        // Draw data points and X labels
        for (i in dataPoints.indices) {
            val x = paddingLeft + spacing * i
            val y = chartHeight - (dataPoints[i] - minValue) / range * chartHeight
            canvas.drawCircle(x, y, 10f, circlePaint)
            if (i < labels.size) {
                canvas.drawText(labels[i], x, height.toFloat(), labelPaint)
            }
        }

        // Draw Y-axis labels (5 segments)
        val ySteps = 5
        for (i in 0..ySteps) {
            val value = maxValue - i * (range / ySteps)
            val y = chartHeight * i / ySteps
            val textY = y + labelPaint.textSize / 2
            canvas.drawText("${value.roundToInt()}", paddingLeft - 10f, textY, yLabelPaint)
        }
    }
}


//
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.graphics.Path
//import android.util.AttributeSet
//import android.view.View
//
//class LineChartView @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null
//) : View(context, attrs) {
//
//    private val paintLine = Paint().apply {
//        color = Color.parseColor("#6200EE") // Purple line
//        strokeWidth = 6f
//        style = Paint.Style.STROKE
//        isAntiAlias = true
//    }
//
//    private val paintFill = Paint().apply {
//        color = Color.parseColor("#996200EE") // Translucent fill
//        style = Paint.Style.FILL
//        isAntiAlias = true
//    }
//
//    private val circlePaint = Paint().apply {
//        color = Color.parseColor("#6200EE")
//        style = Paint.Style.FILL
//        isAntiAlias = true
//    }
//
//    var dataPoints: List<Float> = emptyList()
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        if (dataPoints.isEmpty()) return
//
//        val path = Path()
//        val fillPath = Path()
//
//        val spacing = width.toFloat() / (dataPoints.size - 1)
//        val maxValue = dataPoints.maxOrNull() ?: 0f
//        val minValue = dataPoints.minOrNull() ?: 0f
//        val range = (maxValue - minValue).takeIf { it != 0f } ?: 1f
//
//        // First point
//        val startX = 0f
//        val startY = height - (dataPoints[0] - minValue) / range * height
//        path.moveTo(startX, startY)
//        fillPath.moveTo(startX, height.toFloat())
//        fillPath.lineTo(startX, startY)
//
//        for (i in 1 until dataPoints.size) {
//            val x = spacing * i
//            val y = height - (dataPoints[i] - minValue) / range * height
//            path.lineTo(x, y)
//            fillPath.lineTo(x, y)
//        }
//
//        fillPath.lineTo(spacing * (dataPoints.size - 1), height.toFloat())
//        fillPath.close()
//
//        canvas.drawPath(fillPath, paintFill)
//        canvas.drawPath(path, paintLine)
//
//        // Draw circles
//        for (i in dataPoints.indices) {
//            val x = spacing * i
//            val y = height - (dataPoints[i] - minValue) / range * height
//            canvas.drawCircle(x, y, 10f, circlePaint)
//        }
//    }
//}
