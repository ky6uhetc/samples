package org.example.ru.futurio.command

import org.example.ru.futurio.model.Axis.*
import org.example.ru.futurio.model.Positioning
import org.example.ru.futurio.model.Rotation
import org.example.ru.futurio.model.Turnable

class TurnCommand(
    override val subject: Turnable,
    private val rotation: Rotation?
) : ActionCommand<Turnable> {
    override fun execute() {
        subject.velocity = checkNotNull(subject.velocity) {
            "Can't turn: velocity is not defined"
        }.let { v ->
            when (rotation?.axis) {
                Z -> Positioning(
                    x = v.x * cos(rotation.angle) - v.y * sin(rotation.angle),
                    y = v.x * sin(rotation.angle) + v.y * cos(rotation.angle),
                    z = v.z
                )
                Y -> Positioning(
                    x = v.x * cos(rotation.angle) - v.z * sin(rotation.angle),
                    y = v.y,
                    z = v.x * sin(rotation.angle) + v.z * cos(rotation.angle)
                )
                X -> Positioning(
                    x = v.x,
                    y = v.z * cos(rotation.angle) - v.z * sin(rotation.angle),
                    z = v.z * sin(rotation.angle) + v.y * cos(rotation.angle)
                )
                else -> error("Can't turn: rotation is not defined")
            }
        }
    }
    
    private fun cos(degrees: Double): Double = Math.cos(Math.toRadians(degrees)).round()
    
    private fun sin(degrees: Double): Double = Math.sin(Math.toRadians(degrees)).round()

    private fun Double.round() = "%.15f".format(this).toDouble()
}
