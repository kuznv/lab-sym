package parser

import model.expression.Expression
import model.expression.Id
import model.expression.Num
import model.expression.Operator.ArithmeticFunction.*
import model.expression.Operator.Function
import model.utils.get
import org.w3c.dom.Element

object XmlParser {
    fun parse(element: Element): Expression {
        val kind = element.childNodes["expKind"].single()
        val value = element.childNodes["value"].single()

//        println("kind:${kind.textContent} value:${value.textContent}")

        fun err(message: Any): Nothing = error("$message\nkind:${kind.textContent}\nvalue:${value.textContent}")

        return when (kind.textContent) {
            "binary" -> {
                val left = element.childNodes["left"].single() as Element
                val right = element.childNodes["right"].single() as Element
                val children = listOf(parse(left), parse(right))

                when (value.textContent) {
                    "+" -> Sum(children)
                    "*" -> Mul(children)
                    else -> err(value.textContent)
                }
            }
            "unary" -> {
                val left = element.childNodes["left"].single() as Element
                val children = listOf(parse(left))

                when (value.textContent) {
                    "-" -> Neg(children)
                    else -> err(element)
                }
            }
            "function" -> {
                val args = element.getElementsByTagName("arg")
                val children = List(args.length) { i -> args.item(i) }
                    .map { parse(it as Element) }
                Function(listOf(Id(value.textContent)) + children)
            }
            "value" -> {
                val number = value.textContent.toIntOrNull()
                if (number != null) {
                    Num(number)
                } else {
                    Id(value.textContent)
                }
            }
            else -> err(element)
        }
    }
}