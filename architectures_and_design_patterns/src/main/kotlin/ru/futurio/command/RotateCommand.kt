package ru.futurio.command

import ru.futurio.model.Axis.*
import ru.futurio.model.Positioning
import ru.futurio.model.Rotatable
import ru.futurio.model.Rotation
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP

class RotateCommand(
    override val subject: Rotatable,
    private val rotation: Rotation?
) : ActionCommand<Rotatable> {
    override fun execute() {
        subject.velocity = checkNotNull(subject.velocity) {
            "Can't rotate: velocity is not defined"
        }.let { v ->
            when (rotation?.axis) {
                Z -> Positioning(
                    x = v.x * cos(rotation.angle) - v.y * sin(rotation.angle),
                    y = v.x * sin(rotation.angle) + v.y * cos(rotation.angle),
                    z = v.z
                )
                Y -> Positioning(
                    x = v.x * cos(rotation.angle) + v.z * sin(rotation.angle),
                    y = v.y,
                    z = -v.x * sin(rotation.angle) + v.z * cos(rotation.angle)
                )
                X -> Positioning(
                    x = v.x,
                    y = v.y * cos(rotation.angle) - v.z * sin(rotation.angle),
                    z = v.y * sin(rotation.angle) + v.z * cos(rotation.angle)
                )
                else -> error("Can't rotate: rotation is not defined")
            }
        }
    }
    
    private fun cos(degrees: Double): Double = Math.cos(Math.toRadians(degrees)).round()
    
    private fun sin(degrees: Double): Double = Math.sin(Math.toRadians(degrees)).round()

    private fun Double.round() = BigDecimal.valueOf(this).setScale(8, HALF_UP).toDouble()
}
