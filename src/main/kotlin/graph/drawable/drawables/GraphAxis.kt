package graph.drawable.drawables

import graph.canvas.CanvasElement
import graph.canvas.CanvasView
import graph.drawable.Drawable
import graph.drawable.TextStyle
import model.utils.AnsiCode.Color.GREEN
import model.utils.AnsiCode.Intensity.HIGH_INTENSITY
import java.text.DecimalFormat

class GraphAxis(
    private val textStyle: TextStyle,
    private val decimalFormat: DecimalFormat,
    private val top: Boolean = true,
    private val bottom: Boolean = true,
    private val left: Boolean = true,
    private val right: Boolean = true
) : Drawable {

    override fun draw(view: CanvasView) {
        val canvas = view.canvas
        if (left || right) {
            for (canvasY in 1 until canvas.lastYIndex) {
                val graphY = view.yScaler.canvasToGraph(canvasY.toDouble())
                val canvasElement = CanvasElement(" ${decimalFormat.format(graphY)} ", textStyle)
                if (left) canvas[0, canvasY] = canvasElement
                if (right) canvas[canvas.width - canvasElement.symbols.length, canvasY] = canvasElement
            }
        }
        if (top || bottom) {
            var canvasX = 3
            while (true) {
                val graphX = view.xScaler.canvasToGraph(canvasX.toDouble())
                val canvasElement = CanvasElement("${decimalFormat.format(graphX)} ", textStyle)

                if (top) canvas[canvasX, canvas.lastYIndex] = canvasElement
                if (bottom) canvas[canvasX, 0] = canvasElement

                canvasX += canvasElement.symbols.length
                if (canvasX >= canvas.lastXIndex) break
            }
        }

        val xy = CanvasElement(" Y \\ X", TextStyle(GREEN, intensity = HIGH_INTENSITY))
        canvas[0, canvas.lastYIndex] = xy
        canvas[0, 0] = xy
    }
}