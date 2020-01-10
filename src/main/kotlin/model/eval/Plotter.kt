package model.eval

import graph.drawable.drawables.GraphLine
import graph.drawable.drawables.GraphPoint
import model.expression.Expression
import model.expression.Num
import model.expression.Operator.Drawable
import model.expression.Operator.Drawable.Point
import model.expression.Operator.Drawable.Line
import model.expression.UnitExpr
import graph.GraphFacade
import graph.drawable.drawables.GraphLabel
import model.utils.asIterableOf
import model.utils.toLabel

object Plotter {
    fun plot(drawables: Iterable<Drawable>): UnitExpr? {
        fun Expression.toPoint() = children
            .takeIf { it.size == 2 }
            ?.asIterableOf<Num>()
            ?.map(Num::value)
            ?.map(Int::toDouble)

        val graphDrawables = drawables.map {
            when (it) {
                is Point -> {
                    val (x, y) = it.toPoint() ?: return null

                    GraphPoint(x, y, GraphFacade.pointStyle)
                }
                is Line -> {
                    val (p1, p2) = it.children
                        .takeIf { it.size == 2 }
                        ?.map { it.toPoint() ?: return null }
                        ?: return null
                    val (x1, y1) = p1
                    val (x2, y2) = p2
                    GraphLine(x1, y1, x2, y2, GraphFacade.pointStyle)
                }
            }
        }

        GraphFacade.showGraph(graphDrawables + GraphLabel(10, 3, GraphFacade.pointStyle.toLabel("Lines")))
        return UnitExpr
    }
}