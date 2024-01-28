package ru.futurio.command

import ru.futurio.model.ability.impl.MovableAdapter
import ru.futurio.model.manipulation.RotateCommandStartable

class StartRotateCommand(
    override val subject: RotateCommandStartable
) : Command<RotateCommandStartable> {

    override fun execute(context: CommandContext) {
        val manipulatedObject = checkNotNull(subject.manipulatedObjectId) {
            "Manipulated object ID is not defined for start rotate command"
        }.let { id ->
            requireNotNull(context.getUObjectById(id)) { "Manipulated object not found by ID" }
        }
        val direction = checkNotNull(subject.rotation) {
            "Start rotate direction is not defined"
        }
        checkNotNull(subject.commandQueue) {
            "Command queue is not defined for start rotate command"
        }.add(RotateCommand(MovableAdapter(manipulatedObject), direction))
    }
}
