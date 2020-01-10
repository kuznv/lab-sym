import model.context.Context
import model.eval.ArithmeticEvaluator
import model.eval.BoolEvaluator
import model.eval.Evaluator
import model.expression.Id
import model.expression.Num
import model.expression.Operator.Lisst
import model.expression.UnitExpr
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.TextParser
import parser.XmlParser
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class Test {
    private val x = Id("x")
    private val y = Id("y")
    private val a = Id("a")
    private val b = Id("b")
    private val tmp = Id("tmp")

    @Test
    fun `test xml parsing`() {
        val document = DocumentBuilderFactory
            .newDefaultInstance()
            .newDocumentBuilder()
            .parse(File("sampleInput.xml"))

        val expression = XmlParser.parse(document.documentElement)
        assertEquals(
            "Sum(children=[Mul(children=[2, Mul(children=[b, x])]), Mul(children=[a, x])]), Sum(children=" +
                    "[Mul(children=[6, Mul(children=[b, y])]), Mul(children=[3, Mul(children=[a, pow[x, 2]])])])",
            expression.children.joinToString()
        )
    }

    @Test
    fun `test bool evaluator`() = BoolEvaluator.run {
        assertEquals(true.b, visit(!(false.b)))
    }

    @Test
    fun `test arithmetic evaluator`() = ArithmeticEvaluator.run {
        assertEquals(6.n, visit(2.n * 3.n))
        assertEquals(3.n, visit(1.n + 2.n))
    }

    @Test
    fun `integration test`() = Evaluator(Context()).run {
        val sum = Id("sum")

        assertEquals(
            7.n,
            visit(
                Seq(
                    Set(x, 2.n),
                    Delayed(sum, y, block = y + y),
                    If(sum(x) greater Seq(5.n), UnitExpr, Seq(2.n, Seq(x), 5.n + 2.n))
                )
            )
        )

        assertEquals(
            10.n,
            visit(
                Seq(
                    Set(x, Num(0)),
                    While(x less 10.n, Set(x, x + 1.n)),
                    Seq(
                        Set(tmp, 10.n),
                        tmp
                    )
                )
            )
        )

        val range = Id("range")
        val step = Id("step")
        val result = Id("result")
        assertEquals(
            Lisst((1..9).map(::Num)),
            visit(
                Seq(
                    Delayed(
                        range, a, b, step, block = Block(
                            Set(result, Lisst()),
                            While(
                                a less b, Seq(
                                    Set(result, Append(result, a)),
                                    Set(a, a + step)
                                )
                            ),
                            result
                        )
                    ),
                    range(1.n, 10.n, 1.n)
                )
            )
        )
    }

    @Test
    fun `test text parser`() = TextParser.run {
        assertEquals(
            5.n,
            parse(" 5 ")
        )
        assertEquals(
            Seq(If(true.b, Seq(1.n, 2.n), 2.n), Point(Seq(1.n, 2.n), 2.n)),
            parse("Seq(If(true,   Seq(1, 2), 2) , Point(Seq(1, 2), 2)) ")
        )
    }

    @Test
    fun `example test`() {


        val expression = TextParser.parse(
            """Seq(
        Delayed(
            Id("f"),
            Seq(
                Set(Id("x"), 0),
                Set(Id("r1"), Lisst(Point(0, 0))),
                Set(Id("r2"), Lisst(Point(0, 10))),
                While(
                    Less(Id("x"), 10), Seq(
                        Set(Id("x"), Sum(Id("x"), 1)),
                        Set(Id("r1"), Append(Id("r1"), Point(Id("x"), Mul(Id("x"), Id("x"))))),
                        Set(Id("r2"), Append(Id("r2"), Point(Id("x"), Sub(10, Mul(Id("x"), Id("x"))))))
                    )
                ),
                AppendList(Id("r1"), Id("r2"))
            )
        ),
        Plot(Function(Id("f")))
    )""".trimIndent()
        )

        val evaluator = Evaluator(Context())
        evaluator.visit(expression)

    }
}