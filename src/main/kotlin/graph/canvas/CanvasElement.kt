package graph.canvas

import graph.drawable.TextStyle

data class CanvasElement(val symbols: String, val textStyle: TextStyle = TextStyle.DEFAULT) {
    override fun toString() = textStyle.invoke(symbols)
}