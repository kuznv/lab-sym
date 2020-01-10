package graph

import graph.canvas.CanvasView
import graph.drawable.Drawable

class Graph(var drawables: Iterable<Drawable>, var view: CanvasView) {
    fun paint() {
        drawables.forEach {
            it.draw(view)
        }

        view.canvas
                .asReversed()
                .map { row -> row.joinToString(separator = "") { it ?: " " } }
                .forEach(::println)
    }
}