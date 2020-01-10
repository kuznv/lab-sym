package graph.canvas

import kotlin.math.roundToInt

class Canvas private constructor(list: CanvasList) : CanvasList by list {
    constructor(height: Int, width: Int) : this(
            List(height) {
                MutableList<String?>(width) { null }
            }
    )

    val lastXIndex = first().lastIndex
    val lastYIndex = lastIndex

    val height = lastYIndex + 1
    val width = lastXIndex + 1

    val xyScale = width.toDouble() / height

    val xIndices = first().indices
    val yIndices = indices

    operator fun set(x: Double, y: Double, element: CanvasElement) {
        if (!x.isFinite() || !y.isFinite()) return
        this[x.roundToInt(), y.roundToInt()] = element
    }

    operator fun set(x: Int, y: Int, element: CanvasElement) {
        if (y !in yIndices) return
        if (x >= lastXIndex) return

        val (string, drawParams) = element
        val row = this[y]

        string.indices
                .filter { it + x in xIndices }
                .forEach { i ->
                    row[x + i] = drawParams(string[i].toString())
                }
    }
}