package ru.futurio.math

import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

class QuadraticEquation {

    fun solve(a: Double, b: Double, c: Double): Pair<Double, Double>? {
        require(a.isZero().not()) {
            "'a' param cannot be zero"
        }
        val D = (b.pow(2) - 4 * a * c).also {
            check(it.isNaN().not()) { "D is not a number" }
            check(it.isInfinite().not()) { "D is infinite" }
        }
        return when {
            D < -Double.MIN_VALUE -> null
            b.isZero() -> sqrt(-c / a).let { it to -it }
            D.isZero() -> (-b / (2 * a)).let { it to it }
            else -> error("Uncovered case")
        }
    }

    private fun Double.isZero() = this.absoluteValue < Double.MIN_VALUE
}
