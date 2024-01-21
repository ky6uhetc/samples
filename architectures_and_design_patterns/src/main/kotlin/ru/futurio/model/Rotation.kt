package org.example.ru.futurio.model

data class Rotation(
    val angle: Double,
    val axis: Axis
)

enum class Axis {
    X,Y,Z
}