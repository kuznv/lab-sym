import model.expression.Bool
import model.expression.Expression
import model.expression.Id
import model.expression.Num
import model.expression.Operator.*
import model.expression.Operator.ArithmeticFunction.*
import model.expression.Operator.BoolOperator.*
import model.expression.Operator.Function
import model.expression.Operator.Drawable.*
import model.expression.Operator.Set

fun Sum(vararg children: Expression) = Sum(children.toList())
fun Sub(vararg children: Expression) = Sub(children.toList())
fun Mul(vararg children: Expression) = Mul(children.toList())
fun Div(vararg children: Expression) = Div(children.toList())
fun Rem(vararg children: Expression) = Rem(children.toList())
fun Pow(vararg children: Expression) = Pow(children.toList())
fun Neg(vararg children: Expression) = Neg(children.toList())

fun And(vararg children: Expression) = And(children.toList())
fun Or(vararg children: Expression) = Or(children.toList())
fun Xor(vararg children: Expression) = Xor(children.toList())
fun Equal(a: Expression, b: Expression) = Equal(listOf(a, b))
fun NotEqual(a: Expression, b: Expression) = NotEqual(listOf(a, b))
fun Greater(a: Expression, b: Expression) = Greater(listOf(a, b))
fun GreaterOrEqual(a: Expression, b: Expression) = GreaterOrEqual(listOf(a, b))
fun Less(a: Expression, b: Expression) = Less(listOf(a, b))
fun LessOrEqual(a: Expression, b: Expression) = LessOrEqual(listOf(a, b))
fun Not(a: Expression) = Not(listOf(a))

fun Point(vararg children: Expression) = Point(children.toList())
fun Line(vararg children: Expression) = Line(children.toList())

fun Plot(vararg children: Expression) = Plot(children.toList())
fun If(condition: Expression, ifTrue: Expression, ifFalse: Expression) = If(listOf(condition, ifTrue, ifFalse))
fun While(condition: Expression, block: Expression) = While(listOf(condition, block))
fun Set(id: Expression, value: Expression) = Set(listOf(id, value))
fun Delayed(id: Expression, vararg children: Expression, block: Expression) =
    Delayed(listOf(id) + children.toList() + block)

fun Function(id: Expression, vararg children: Expression) = Function(listOf(id) + children.toList())
fun Seq(vararg children: Expression) = Seq(children.toList())
fun Block(vararg children: Expression) = Block(children.toList())
fun Append(vararg children: Expression) = Append(children.toList())
fun AppendList(vararg children: Expression) = AppendList(children.toList())
fun Lisst(vararg children: Expression) = Lisst(children.toList())

infix fun Expression.greater(other: Expression) = Greater(listOf(this, other))
infix fun Expression.greaterOrEqual(other: Expression) = GreaterOrEqual(listOf(this, other))
infix fun Expression.less(other: Expression) = Less(listOf(this, other))
infix fun Expression.lessOrEqual(other: Expression) = LessOrEqual(listOf(this, other))

operator fun Id.invoke(vararg children: Expression) = Function(listOf(this) + children.toList())
operator fun Bool.not() = Not(this)
operator fun Expression.plus(other: Expression) = Sum(this, other)
operator fun Expression.times(other: Expression) = Mul(this, other)
operator fun Expression.minus(other: Expression) = Sub(this, other)
operator fun Num.unaryMinus() = Neg(this)

val Int.n get() = Num(this)
val Boolean.b get() = Bool(this)
