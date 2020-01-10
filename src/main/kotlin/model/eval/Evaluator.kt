package model.eval

import model.context.Context
import model.expression.*
import model.expression.Operator.*
import model.expression.Operator.Function
import model.expression.Operator.Set
import model.utils.asIterableOf

class Evaluator(parentContext: Context) : ExpressionVisitor {
    private val context = parentContext.toMutableMap()

    private fun acceptFully(expression: Expression): Expression? {
        var current = expression.accept(this) ?: return null
        while (true) {
            current = current.accept(this) ?: return current
        }
    }

    private fun <T : Expression> acceptChildren(expression: T): T? {
        var updated = false
        val newChildren = expression.children.map { acceptFully(it)?.also { updated = true } ?: it }
        if (!updated) return null

        @Suppress("UNCHECKED_CAST")
        return expression.clone(newChildren) as T
    }

    override fun visit(arithmeticFunction: ArithmeticFunction): Expression? {
        val updatedExpression = acceptChildren(arithmeticFunction)
            ?: return ArithmeticEvaluator.visit(arithmeticFunction)
        return ArithmeticEvaluator.visit(updatedExpression) ?: updatedExpression
    }

    override fun visit(boolOperator: BoolOperator): Expression? {
        val updatedExpression = acceptChildren(boolOperator)
            ?: return BoolEvaluator.visit(boolOperator)
        return BoolEvaluator.visit(updatedExpression) ?: updatedExpression
    }

    override fun visit(`if`: If): Expression? {
        val args = `if`.children
        if (args.size != 3) return null

        val (condition, ifTrue, ifFalse) = args
        val updatedCondition = (acceptFully(condition) ?: condition) as? Bool ?: return null

        val result = if (updatedCondition.value) ifTrue else ifFalse
        return result.accept(this)
    }

    override fun visit(`while`: While): Expression? {
        val args = `while`.children
        if (args.size != 2) return null

        val (condition, body) = `while`.children
        do {
            val updatedCondition = (acceptFully(condition) ?: condition) as? Bool ?: return null
            if (!updatedCondition.value) break

            acceptFully(body)
        } while (true)

        return UnitExpr
    }

    override fun visit(identifier: Id): Expression? =
        (context[identifier] as? Expression)?.let { acceptFully(it) ?: it } ?: error(identifier)

    override fun visit(set: Set): UnitExpr? {
        val args = set.children
        if (args.size != 2) return null

        val (identifier, value) = args
        if (identifier !is Id) return null

        context[identifier] = acceptFully(value) ?: value
        return UnitExpr
    }

    private data class ParsedFunction(val id: Id, val argsIds: Iterable<Id>, val block: Expression)

    override fun visit(delayed: Delayed): UnitExpr? {
        val children = delayed.children
        if (children.size < 2) return null

        val id = children.first() as? Id ?: return null
        val args = children.subList(1, children.lastIndex).asIterableOf<Id>() ?: return null
        val body = children.last()

        context[id] = ParsedFunction(id, args, body)
        return UnitExpr
    }

    override fun visit(function: Function): Expression? {
        val id = function.children.firstOrNull() as? Id ?: return null
        val argsValues = function.children.drop(1)
        val (_, argsIds, block) = context[id] as? ParsedFunction ?: return null
        val args = argsIds.zip(argsValues).toMap()

        val newContext = Context(context + args)
        val newEvaluator = Evaluator(newContext)
        return newEvaluator.acceptFully(block)
    }

    override fun visit(block: Block): Expression {
        val newContext = Context(context)
        val newEvaluator = Evaluator(newContext)
        return block.children.map { newEvaluator.acceptFully(it) ?: it }.last()
    }

    override fun visit(seq: Seq): Expression? =
        seq.children.map { it.accept(this) ?: it }.last()

    override fun visit(append: Append): Expression? {
        val children = append.children
        val container = children.firstOrNull() ?: return null
        val updatedContainer = acceptFully(container) ?: container
        val updatedChildren = children.drop(1).map { acceptFully(it) ?: it }
        return updatedContainer.clone(updatedContainer.children + updatedChildren)
    }

    override fun visit(lisst: Lisst): Expression? {
        return acceptChildren(lisst)
    }

    override fun visit(appendList: AppendList): Expression? {
        val children = appendList.children
        if(children.size != 2) return null

        val (container1, container2) = children
        val updatedContainer1 = acceptFully(container1) ?: container1
        val updatedContainer2 = acceptFully(container2) ?: container2
        return updatedContainer1.clone(updatedContainer1.children + updatedContainer2.children)
    }

    override fun visit(point: Drawable.Point): Expression? = acceptChildren(point)

    override fun visit(line: Drawable.Line): Expression? = acceptChildren(line)

    override fun visit(plot: Plot): Expression? {
        val updatedPlot = acceptChildren(plot)
        val drawables = (updatedPlot ?: plot).children.firstOrNull()?.children?.asIterableOf<Drawable>() ?: return updatedPlot
        return Plotter.plot(drawables)
    }
}