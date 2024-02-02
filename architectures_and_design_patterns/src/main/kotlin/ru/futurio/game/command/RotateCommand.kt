package ru.futurio.game.command

import ru.futurio.game.model.Axis.*
import ru.futurio.game.model.Positioning
import ru.futurio.game.model.Rotation
import ru.futurio.game.model.ability.Movable
import ru.futurio.game.util.MovementUtil.cos
import ru.futurio.game.util.MovementUtil.sin

class RotateCommand(
    override val subject: Movable,
    private val rotation: Rotation?
) : InternalCommand<Movable> {
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
