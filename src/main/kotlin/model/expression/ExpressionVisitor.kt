package model.expression

import model.expression.Operator.*
import model.expression.Operator.ArithmeticFunction.*
import model.expression.Operator.BoolOperator.*
import model.expression.Operator.Function
import model.expression.Operator.Drawable.*
import model.expression.Operator.Set

interface ExpressionVisitor {
    fun visit(expression: Expression): Expression? = null

    fun visit(arithmeticFunction: ArithmeticFunction) = visit(expression = arithmeticFunction)
    fun visit(sum: Sum) = visit(arithmeticFunction = sum)
    fun visit(sub: Sub) = visit(arithmeticFunction = sub)
    fun visit(mul: Mul) = visit(arithmeticFunction = mul)
    fun visit(div: Div) = visit(arithmeticFunction = div)
    fun visit(rem: Rem) = visit(arithmeticFunction = rem)
    fun visit(pow: Pow) = visit(arithmeticFunction = pow)
    fun visit(neg: Neg) = visit(arithmeticFunction = neg)
    fun visit(and: And) = visit(arithmeticFunction = and)
    fun visit(or: Or) = visit(arithmeticFunction = or)
    fun visit(xor: Xor) = visit(arithmeticFunction = xor)

    fun visit(boolOperator: BoolOperator) = visit(expression = boolOperator)
    fun visit(equal: Equal) = visit(boolOperator = equal)
    fun visit(notEqual: NotEqual) = visit(boolOperator = notEqual)
    fun visit(greater: Greater) = visit(boolOperator = greater)
    fun visit(greaterOrEqual: GreaterOrEqual) = visit(boolOperator = greaterOrEqual)
    fun visit(less: Less) = visit(boolOperator = less)
    fun visit(lessOrEqual: LessOrEqual) = visit(boolOperator = lessOrEqual)
    fun visit(not: Not) = visit(boolOperator = not)

    fun visit(drawable: Drawable) = visit(expression = drawable)
    fun visit(point: Point) = visit(drawable = point)
    fun visit(line: Line) = visit(drawable = line)

    fun visit(lisst: Lisst) = visit(expression = lisst)

    fun visit(plot: Plot) = visit(expression = plot)
    fun visit(`if`: If) = visit(expression = `if`)
    fun visit(`while`: While) = visit(expression = `while`)
    fun visit(set: Set) = visit(expression = set)
    fun visit(delayed: Delayed) = visit(expression = delayed)
    fun visit(identifier: Id) = visit(expression = identifier)
    fun visit(function: Function) = visit(expression = function)
    fun visit(seq: Seq) = visit(expression = seq)
    fun visit(block: Block) = visit(expression = block)
    fun visit(append: Append) = visit(expression = append)
    fun visit(appendList: AppendList) = visit(expression = appendList)
}