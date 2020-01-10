package graph.drawable.drawables

import graph.canvas.CanvasElement
import graph.canvas.CanvasView
import graph.drawable.Drawable

class GraphLabel(val x: Int, val y: Int, val element: CanvasElement) : Drawable {
    override fun draw(view: CanvasView) {
        view.canvas[x, y] = element
    }
}