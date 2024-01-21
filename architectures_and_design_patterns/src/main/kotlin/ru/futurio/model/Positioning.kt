package org.example.ru.futurio.model

data class Positioning(
    val x: Double,
    val y: Double,
    val z: Double = 0.0
) {

    fun add(shift: Positioning): Positioning {
        return Positioning(
            x + shift.x,
            y + shift.y,
            z + shift.z
        )
    }
}
