package ru.futurio.math

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class QuadraticEquationTest {

    @Test
    fun `no solution`() {
        assertNull(
            QuadraticEquation().solve(1.0, 0.0, 1.0)
        )
    }

    @Test
    fun `pair with the same abs value`() {
        assertEquals(
            1.0 to -1.0,
            QuadraticEquation().solve(1.0, 0.0, -1.0)
        )
    }

    @Test
    fun `single solution`() {
        assertEquals(
            -1.0 to -1.0,
            QuadraticEquation().solve(1.0, 2.0, 1.0)
        )
    }

    @Test
    fun `zero a is not valid`() {
        assertThrows<IllegalArgumentException> {
            QuadraticEquation().solve(0.0, 2.0, 1.0)
        }.also { ex ->
            assertEquals(
                "'a' param cannot be zero",
                ex.message
            )
        }
    }

    @ParameterizedTest
    @MethodSource("nanArguments")
    fun `non-numeric values cases`(a: Double, b: Double, c: Double) {
        assertThrows<IllegalStateException> {
            QuadraticEquation().solve(a, b, c)
        }.also { ex ->
            assertEquals(
                "D is not a number",
                ex.message
            )
        }
    }

    @ParameterizedTest
    @MethodSource("infiniteArguments")
    fun `infinitive values cases`(a: Double, b: Double, c: Double) {
        assertThrows<IllegalStateException> {
            QuadraticEquation().solve(a, b, c)
        }.also { ex ->
            assertEquals(
                "D is infinite",
                ex.message
            )
        }
    }

    companion object {

        @JvmStatic
        fun nanArguments() = listOf(
            Arguments.of(Double.NaN, 1.0, 1.0),
            Arguments.of(1.0, Double.NaN, 1.0),
            Arguments.of(1.0, 1.0, Double.NaN)
        )

        @JvmStatic
        fun infiniteArguments() = listOf(
            Arguments.of(Double.POSITIVE_INFINITY, 1.0, 1.0),
            Arguments.of(-Double.POSITIVE_INFINITY, 1.0, 1.0),
            Arguments.of(1.0, Double.POSITIVE_INFINITY, 1.0),
            Arguments.of(1.0, -Double.POSITIVE_INFINITY, 1.0),
            Arguments.of(1.0, 1.0, Double.POSITIVE_INFINITY),
            Arguments.of(1.0, 1.0, -Double.POSITIVE_INFINITY)
        )
    }
}
