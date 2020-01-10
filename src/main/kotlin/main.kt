import model.context.Context
import model.eval.Evaluator
import model.expression.Bool
import model.expression.Id
import model.expression.UnitExpr
import parser.TextParser

fun main() {
    val evaluator = Evaluator(Context())

    val expr = Seq(
        Delayed(Id("if"), Bool(true), Id("ifTrue"), UnitExpr, block = Id("ifTrue")),
        Delayed(Id("if"), Bool(false), UnitExpr, Id("ifFalse"), block = Id("ifFalse")),
        Delayed(Id("while"), Bool(false), block = UnitExpr),
        Delayed(Id("while"), Id("cond"), Id("block"), block = Seq(Function(Id("block")), Function(Id("while"), Id("cond"), Id("block")))),
        Delayed(
            Id("f"),
            block = Seq(
                Set(Id("x"), 0.n),
                Set(Id("r1"), Lisst(Point(0.n, 0.n))),
                Set(Id("r2"), Lisst(Point(0.n, 10.n))),
                While(
                    Less(Id("x"), 10.n), Seq(
                        Set(Id("x"), Sum(Id("x"), 1.n)),
                        Set(Id("r1"), Append(Id("r1"), Point(Id("x"), Mul(Id("x"), Id("x"))))),
                        Set(Id("r2"), Append(Id("r2"), Point(Id("x"), Sub(10.n, Mul(Id("x"), Id("x"))))))
                    )
                ),
                AppendList(Id("r1"), Id("r2"))
            )
        ),
        Plot(Function(Id("f")))
    )

    println(expr)
    println(evaluator.visit(expr))
}

fun main2() {
//    repl()

    val evaluator = Evaluator(Context())
    val lines = Id("lines")
    val a = Id("a")

    evaluator.visit(
        Seq(
            Set(a, 1.n),
            Set(lines, Lisst()),
            While(
                a less 10.n,
                Seq(
                    Set(a, a + 1.n),
                    Set(
                        lines,
                        Append(
                            lines, Line(
                                Point((-5).n, (-5).n),
                                Point(10.n, (a - 4.n) * 5.n)
                            )
                        )

                    )
                )
            ),
            Plot(lines)
        )
    )
}

fun repl() {
    val evaluator = Evaluator(Context())

    while (true) {
        val input = generateSequence(::readLine).takeWhile { it.isNotBlank() }.joinToString()
        val expression = TextParser.parse(input)
        println(evaluator.visit(expression))
    }
}