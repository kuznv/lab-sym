package parser

import model.expression.*
import model.utils.allNestedClasses
import model.utils.orElse
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

object TextParser {
    @Suppress("UNCHECKED_CAST")
    private val functions =
        Operator::class.allNestedClasses().associate { it.simpleName!! to it.primaryConstructor!! }

    fun parse(s: String): Expression {
        val input = s.trim()

        input.toIntOrNull()?.let { return Num(it) }

        when (input.toLowerCase()) {
            "true" -> return Bool(true)
            "false" -> return Bool(false)
        }

        val name = input.substringBefore("(").takeIf { it != input } ?: error("No name")


        val argsText = input.substring(name.length)
            .takeIf { it.startsWith('(') }.orElse { error("No '('") }
            .takeIf { it.endsWith(')') }.orElse { error("No ')'") }
            .drop(1).dropLast(1)

        if (name.toLowerCase() == "id") return Id(argsText)

        val commaIndices = mutableListOf(-1)
        argsText.foldIndexed(0) { i, depth, c ->
            when (c) {
                '(' -> depth + 1
                ')' -> depth - 1
                ',' -> depth.also { if (it == 0) commaIndices += i }
                else -> depth
            }
        }
        commaIndices += argsText.length

        val args = commaIndices.zipWithNext { i1, i2 -> argsText.substring(i1 + 1, i2) }.map(String::trim)

        @Suppress("UNCHECKED_CAST")
        val operator = functions[name] ?: error("Unknown operator '$name'")
        return operator.call(args.map(::parse))
    }
}