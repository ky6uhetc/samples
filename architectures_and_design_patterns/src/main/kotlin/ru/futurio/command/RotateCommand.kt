package ru.futurio.command

import ru.futurio.model.Axis.*
import ru.futurio.model.Positioning
import ru.futurio.model.Rotation
import ru.futurio.model.ability.Movable
import ru.futurio.util.MovementUtil.cos
import ru.futurio.util.MovementUtil.sin

class RotateCommand(
    override val subject: Movable,
    private val rotation: Rotation?
) : Command<Movable> {
    override fun execute(context: CommandContext) {
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
}
