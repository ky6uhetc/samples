package ru.futurio.game.util

import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP

object MovementUtil {
    fun cos(degrees: Double): Double = Math.cos(Math.toRadians(degrees)).round()

    fun sin(degrees: Double): Double = Math.sin(Math.toRadians(degrees)).round()

    private fun Double.round() = BigDecimal.valueOf(this).setScale(8, HALF_UP).toDouble()
}
