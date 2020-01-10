package graph.drawable.drawables

import graph.canvas.CanvasElement

data class GraphLine(
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
    override val canvasElement: CanvasElement
) : GraphFunction(canvasElement, { x ->
    if (x !in x1..x2) Double.NaN
    else {
        val k = (y2 - y1) / (x2 - x1)
        y1 + (x - x1) * k
    }
})