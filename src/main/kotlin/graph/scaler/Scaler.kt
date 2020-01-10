package graph.scaler

import model.utils.DoubleRange

class Scaler(
    private val originalRange: DoubleRange,
    private val targetRange: DoubleRange
) : (Double) -> Double {

    override operator fun invoke(value: Double): Double {
        /*
           a..x...b
           c....y......b
        */

        val a = originalRange.start
        val b = originalRange.endInclusive
        val c = targetRange.start
        val d = targetRange.endInclusive

        return ((a * d) - b * c + value * (c - d)) /
                (a - b)
    }

    operator fun not() = Scaler(targetRange, originalRange)
}