package ru.futurio.command

import ru.futurio.model.ability.impl.MovableAdapter
import ru.futurio.model.manipulation.RotateCommandEndable

class StopRotateCommand(
    override val subject: RotateCommandEndable
) : Command<RotateCommandEndable> {
    override fun execute(context: CommandContext) {
        val manipulatedObject = checkNotNull(subject.manipulatedObjectId) {
            "Manipulated object ID is not defined for stop rotate command"
        }.let { id ->
            requireNotNull(context.getUObjectById(id)) { "Manipulated object not found by ID" }
        }
        checkNotNull(subject.commandQueue) {
            "Command queue is not defined for stop rotate command"
        }.removeIf {
            (it as? RotateCommand)?.subject?.id == subject.manipulatedObjectId
        }
        SetMovingCommand(MovableAdapter(manipulatedObject), null).execute(context)
    }
}
