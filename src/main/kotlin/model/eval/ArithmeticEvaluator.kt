package model.eval

import model.expression.ExpressionVisitor
import model.expression.Num
import model.expression.Operator.ArithmeticFunction
import model.expression.Operator.ArithmeticFunction.*
import model.utils.asIterableOf
import kotlin.math.roundToInt

object ArithmeticEvaluator : ExpressionVisitor {
    override fun visit(arithmeticFunction: ArithmeticFunction): Num? = arithmeticFunction.accept(this) as Num?

    private fun ArithmeticFunction.getArgs(): Iterable<Int>? =
        children.takeIf { it.isNotEmpty() }?.asIterableOf<Num>()?.map(Num::value)

    override fun visit(neg: Neg) = neg.getArgs()?.singleOrNull()?.let(Int::unaryMinus)?.let(::Num)
    override fun visit(sum: Sum) = sum.getArgs()?.reduce(Int::plus)?.let(::Num)
    override fun visit(mul: Mul) = mul.getArgs()?.reduce(Int::times)?.let(::Num)
    override fun visit(sub: Sub) = sub.getArgs()?.reduce(Int::minus)?.let(::Num)
    override fun visit(div: Div) = div.getArgs()?.reduce(Int::div)?.let(::Num)
    override fun visit(rem: Rem) = rem.getArgs()?.reduce(Int::rem)?.let(::Num)
    override fun visit(and: And) = and.getArgs()?.reduce(Int::and)?.let(::Num)
    override fun visit(or: Or) = or.getArgs()?.reduce(Int::or)?.let(::Num)
    override fun visit(xor: Xor) = xor.getArgs()?.reduce(Int::xor)?.let(::Num)
    override fun visit(pow: Pow) = pow.getArgs()?.map(Int::toDouble)?.reduce(Math::pow)?.roundToInt()?.let(::Num)
}