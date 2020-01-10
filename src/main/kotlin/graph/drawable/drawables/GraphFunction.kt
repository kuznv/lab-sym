package graph.drawable.drawables

import graph.canvas.CanvasElement
import graph.canvas.CanvasView
import graph.drawable.Drawable

open class GraphFunction(open val canvasElement: CanvasElement, val f: (Double) -> Double) : Drawable {
    override fun draw(view: CanvasView) {
        val canvas = view.canvas
        for (canvasX in canvas.xIndices.map(Int::toDouble)) {
            val graphX = view.xScaler.canvasToGraph(canvasX)
            try {
                val graphY = f(graphX)
                if (!graphY.isFinite()) continue
                val canvasY = view.yScaler.graphToCanvas(graphY)
                canvas[canvasX, canvasY] = canvasElement
            } catch (e: ArithmeticException) {
            }
        }
    }
}