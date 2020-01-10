package model.utils

import graph.Graph
import graph.canvas.Canvas
import graph.canvas.CanvasElement
import graph.canvas.CanvasView
import graph.drawable.drawables.GraphFunction
import model.utils.AnsiCode.Color.*
import model.utils.AnsiCode.Intensity.HIGH_INTENSITY
import model.utils.AnsiCode.SANE

data class NamedFunction(val name: String, val function: GraphFunction)

private val helpMessage = """
        |Перемещение по графику: ${RED("WASD")}
        |Масштаб: ${GREEN("[x/y]")} ${RED("+/-")}
        |Перейти к точке: ${RED("<x> <y>")}
        |Выход: ${RED("q")}
        |$YELLOW$HIGH_INTENSITY> $SANE""".trimMargin()

fun showGraphMenu(graph: Graph, graphFunctions: List<NamedFunction> = emptyList()) {
    graph.paint()

    var input = ""
    do {
        print(helpMessage)

        val view = graph.view
        val xRange = view.xRange
        val yRange = view.yRange
        var x = xRange.middle
        var y = yRange.middle
        val xStep = xRange.length / 10.0
        val yStep = yRange.length / 10.0
        var scale = 1.0

        readLine()?.takeIf { it.isNotEmpty() }?.let { input = it }

        var xRead = false
        var yRead = false
        var xScale = true
        var yScale = true
        for (word in input.split(' ')) {
            val number = word.toDoubleOrNull()
            if (number != null) {
                if (!xRead) {
                    x = number; xRead = true
                } else {
                    y = number; yRead = true
                }
            } else for (char in word) when (char.toLowerCase()) {
                'w' -> y += yStep
                'a' -> x -= xStep
                's' -> y -= yStep
                'd' -> x += xStep
                'x' -> yScale = false
                'y' -> xScale = false
                '+' -> scale *= 0.8
                '-' -> scale *= 1.2
                'q' -> return
            }
        }

        graph.view = CanvasView(
                Canvas(view.canvas.height, view.canvas.width),
                xRange = if (!xScale) xRange else {
                    val r = scale * xRange.length / 2
                    x - r..x + r
                },
                yRange = if (!yScale) yRange else {
                    val r = scale * yRange.length / 2
                    y - r..y + r
                }
        )
        graph.paint()

        if (xRead || yRead) for ((name, function) in graphFunctions) {
            val textStyle = function.canvasElement.textStyle
            println("${textStyle(name)} ($x) = ${function.f(x)}")
        }
    } while (true)
}

fun CanvasElement.toLabel(name: String): CanvasElement {
    val functionSymbols = List(5) { symbols[it % symbols.length] }.joinToString(prefix = " ", separator = "")
    return CanvasElement(name + functionSymbols, textStyle)
}