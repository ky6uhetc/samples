package ru.futurio.command

import ru.futurio.model.Positioning
import ru.futurio.model.manipulation.MoveCommandStartable
import ru.futurio.model.ability.impl.MovableAdapter
import ru.futurio.model.ability.impl.MovingAdapter
import ru.futurio.util.MovementUtil

class StartMoveCommand(
    override val subject: MoveCommandStartable
) : Command<MoveCommandStartable> {

    override fun execute(context: CommandContext) {
        val manipulatedObject = checkNotNull(subject.manipulatedObjectId) {
            "Manipulated object ID is not defined for start move command"
        }.let { id ->
            requireNotNull(context.getUObjectById(id)) { "Manipulated object not found by ID" }
        }
        val direction = checkNotNull(subject.movingDirection) {
            "Start move direction is not defined"
        }
        val velocity = checkNotNull(subject.velocityModulus) {
            "Velocity modulus is not defined"
        }.toDouble().let { v ->
            Positioning(
                x = v * MovementUtil.cos(direction.angleHorizontal) * MovementUtil.cos(direction.angleVertical),
                y = v * MovementUtil.sin(direction.angleHorizontal) * MovementUtil.cos(direction.angleVertical),
                z = v * MovementUtil.sin(direction.angleVertical)
            )
        }
        checkNotNull(subject.commandQueue) {
            "Command queue is not defined for start move command"
        }.let { queue ->
            SetMovingCommand(MovableAdapter(manipulatedObject), velocity).execute(context)
            queue.add(MoveCommand(MovingAdapter(manipulatedObject)))
        }
    }
}
