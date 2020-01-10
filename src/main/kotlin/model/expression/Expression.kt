package model.expression

interface Expression {
    val children: List<Expression>
    fun accept(visitor: ExpressionVisitor): Expression? = null
    fun clone(children: List<Expression>): Expression = this
}

object UnitExpr : Expression {
    override val children get() = emptyList<Nothing>()
    override fun toString() = "UnitExpr"
}

data class Num(val value: Int) : Expression {
    override val children get() = emptyList<Nothing>()
    override fun toString() = value.toString()
}

data class Bool(val value: Boolean) : Expression {
    override val children get() = emptyList<Nothing>()
    override fun toString() = value.toString()
}

data class Id(val name: String) : Expression {
    override val children get() = emptyList<Nothing>()
    override fun toString() = name
    override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
}

sealed class Operator(val name: String, private val _clone: (List<Expression>) -> Expression) : Expression {
    override fun toString() = name + children.joinToString(prefix = "[", postfix = "]")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Operator

        if (name != other.name) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }

    override fun clone(children: List<Expression>) = _clone(children)

    sealed class ArithmeticFunction(name: String, _clone: (List<Expression>) -> Expression) : Operator(name, _clone) {
        data class Sum(override val children: List<Expression>) : ArithmeticFunction("Sum", ::Sum) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Sub(override val children: List<Expression>) : ArithmeticFunction("Sub", ::Sub) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Mul(override val children: List<Expression>) : ArithmeticFunction("Mul", ::Mul) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Div(override val children: List<Expression>) : ArithmeticFunction("Div", ::Div) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Rem(override val children: List<Expression>) : ArithmeticFunction("Rem", ::Rem) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Pow(override val children: List<Expression>) : ArithmeticFunction("Pow", ::Pow) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Neg(override val children: List<Expression>) : ArithmeticFunction("Neg", ::Neg) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class And(override val children: List<Expression>) : ArithmeticFunction("And", ::And) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Or(override val children: List<Expression>) : ArithmeticFunction("Or", ::Or) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Xor(override val children: List<Expression>) : ArithmeticFunction("Xor", ::Xor) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }
    }

    sealed class BoolOperator(name: String, _clone: (List<Expression>) -> Expression) : Operator(name, _clone) {
        data class Equal(override val children: List<Expression>) : BoolOperator("Equal", ::Equal) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class NotEqual(override val children: List<Expression>) : BoolOperator("NotEqual", ::NotEqual) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Greater(override val children: List<Expression>) : BoolOperator("Greater", ::Greater) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class GreaterOrEqual(override val children: List<Expression>) :
            BoolOperator("GreaterOrEqual", ::GreaterOrEqual) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Less(override val children: List<Expression>) : BoolOperator("Less", ::Less) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class LessOrEqual(override val children: List<Expression>) : BoolOperator("LessOrEqual", ::LessOrEqual) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }

        data class Not(override val children: List<Expression>) : BoolOperator("Not", ::Not) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }
    }

    sealed class Drawable(name: String, _clone: (List<Expression>) -> Expression) : Operator(name, _clone) {
        data class Point(override val children: List<Expression>) : Drawable("Point", ::Point) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }
        data class Line(override val children: List<Expression>) : Drawable("Line", ::Line) {
            override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
        }
    }

    data class If(override val children: List<Expression>) : Operator("If", ::If) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    data class While(override val children: List<Expression>) : Operator("While", ::If) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    data class Set(override val children: List<Expression>) : Operator("Set", ::Set) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    data class Delayed(override val children: List<Expression>) : Operator("Delayed", ::Delayed) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    class Function(override val children: List<Expression>) : Operator(children.firstOrNull().toString(), ::Function) {
        override fun toString() = name + children.drop(1).joinToString(prefix = "[", postfix = "]")
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    data class Seq(override val children: List<Expression>) : Operator("Seq", ::Seq) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    data class Block(override val children: List<Expression>) : Operator("Block", ::Block) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    data class Lisst(override val children: List<Expression>) : Operator("Lisst", ::Lisst) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    data class Append(override val children: List<Expression>) : Operator("Append", ::Append) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    data class AppendList(override val children: List<Expression>) : Operator("AppendList", ::Append) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }

    data class Plot(override val children: List<Expression>) : Operator("Plot", ::Plot) {
        override fun accept(visitor: ExpressionVisitor) = visitor.visit(this)
    }
}