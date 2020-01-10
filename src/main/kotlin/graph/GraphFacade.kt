package graph

import graph.canvas.Canvas
import graph.canvas.CanvasElement
import graph.canvas.CanvasView
import graph.drawable.Drawable
import graph.drawable.TextStyle
import graph.drawable.drawables.GraphAxis
import graph.drawable.drawables.GraphLabel
import graph.drawable.drawables.GraphLine
import model.utils.AnsiCode
import model.utils.AnsiCode.Color.BLUE
import model.utils.AnsiCode.Color.RED
import model.utils.NamedFunction
import model.utils.showGraphMenu
import java.text.DecimalFormat

object GraphFacade {
    private const val FRACTION_DIGITS = 2

    private val decimalFormat = DecimalFormat().apply {
        minimumFractionDigits = FRACTION_DIGITS
        maximumFractionDigits = FRACTION_DIGITS
        isGroupingUsed = false
        positivePrefix = " "
    }

    val pointStyle = CanvasElement("*", TextStyle(RED))
    private val graphAxis = GraphAxis(TextStyle(BLUE), decimalFormat, right = false, top = false)

    fun showGraph(drawables: Iterable<Drawable>) {
        val canvas = Canvas(height = 37, width = 197)
        val graph = Graph(
            drawables = drawables + graphAxis /*+ graphLabelsPanel*/,
            view = CanvasView(
                canvas = canvas,
                xRange = -10.0..20.0,
                yRange = -10.0..20.0 /*/ canvas.xyScale*/
            )
        )
        showGraphMenu(graph)
    }
}