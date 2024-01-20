package org.example.ru.futurio.model

import kotlin.math.max

data class Positioning(
    private val values: List<Double>
) {

    constructor(vararg values: Double) : this(values.toList())

    fun add(shift: Positioning): Positioning {
        val newValues = mutableListOf<Double>()
        for (i in 0..max(values.size, shift.values.size) - 1) {
            val v1 = values.getOrNull(i) ?: 0.0
            val v2 = shift.values.getOrNull(i) ?: 0.0
            newValues.add(v1 + v2)
        }
        return Positioning(newValues.toList())
    }
}
