package ru.futurio.game.command

import ru.futurio.game.model.ability.impl.MovableAdapter
import ru.futurio.game.model.manipulation.MoveCommandEndable

class StopMoveCommand(
    override val subject: MoveCommandEndable
) : Command<MoveCommandEndable> {
    override fun execute(context: CommandContext) {
        val manipulatedObject = checkNotNull(subject.manipulatedObjectId) {
            "Manipulated object ID is not defined for stop move command"
        }.let { id ->
            requireNotNull(context.getUObjectById(id)) { "Manipulated object not found by ID" }
        }
        checkNotNull(subject.commandQueue) {
            "Command queue is not defined for stop move command"
        }.removeIf {
            (it as? MoveCommand)?.subject?.id == subject.manipulatedObjectId
        }
        SetMovingCommand(MovableAdapter(manipulatedObject), null).execute(context)
    }
}
