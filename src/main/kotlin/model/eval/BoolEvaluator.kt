package model.eval

import model.expression.Bool
import model.expression.ExpressionVisitor
import model.expression.Num
import model.expression.Operator.BoolOperator
import model.expression.Operator.BoolOperator.*
import model.utils.asIterableOf

object BoolEvaluator : ExpressionVisitor {
    override fun visit(boolOperator: BoolOperator): Bool? = boolOperator.accept(this) as Bool?

    override fun visit(not: Not): Bool? = (not.children.singleOrNull() as? Bool)?.let { Bool(!it.value) }

    private fun visitComparision(boolOperator: BoolOperator, compare: (Int, Int) -> Boolean): Bool? {
        val args = boolOperator.children.asIterableOf<Num>()?.map(Num::value) ?: return null
        return args.asSequence()
            .zipWithNext()
            .all { (a, b) -> compare(a, b) }
            .let(::Bool)
    }

    override fun visit(equal: Equal) = visitComparision(equal) { a, b -> a == b }
    override fun visit(notEqual: NotEqual) = visitComparision(notEqual) { a, b -> a != b }
    override fun visit(greater: Greater) = visitComparision(greater) { a, b -> a > b }
    override fun visit(greaterOrEqual: GreaterOrEqual) = visitComparision(greaterOrEqual) { a, b -> a >= b }
    override fun visit(less: Less) = visitComparision(less) { a, b -> a < b }
    override fun visit(lessOrEqual: LessOrEqual) = visitComparision(lessOrEqual) { a, b -> a <= b }
}