package graph.drawable.drawables

import graph.canvas.CanvasElement
import graph.canvas.CanvasView
import graph.drawable.Drawable

class GraphPoint(
        val x: Double,
        val y: Double,
        val canvasElement: CanvasElement
) : Drawable {
    override fun draw(view: CanvasView) {
        val canvasX = view.xScaler.graphToCanvas(x)
        val canvasY = view.yScaler.graphToCanvas(y)

        view.canvas[canvasX, canvasY] = canvasElement
    }
}