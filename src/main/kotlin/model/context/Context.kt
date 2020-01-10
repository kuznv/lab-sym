package model.context

import model.expression.Expression

class Context(context: Map<Expression, Any> = mapOf()) : Map<Expression, Any> by context

operator fun Context.plus(other: Context) = Context(this as Map<Expression, Any> + other)