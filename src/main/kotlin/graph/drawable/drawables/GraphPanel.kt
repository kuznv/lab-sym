package graph.drawable.drawables

import graph.canvas.CanvasElement
import graph.canvas.CanvasView
import graph.drawable.Drawable

class GraphPanel(
        private val x: Int = 12,
        private val y: Int = 3,
        private val canvasElements: List<CanvasElement>
) : Drawable {
    override fun draw(view: CanvasView) {
        var currentY = y
        canvasElements.map { element ->
            GraphLabel(x, currentY--, element)
        }.forEach { it.draw(view) }
    }
}