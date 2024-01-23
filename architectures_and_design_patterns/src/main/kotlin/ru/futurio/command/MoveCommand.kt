package org.example.ru.futurio.command

import org.example.ru.futurio.model.Movable

class MoveCommand(
    override val subject: Movable
) : ActionCommand<Movable> {
    override fun execute() {
        subject.position = checkNotNull(subject.position) { "Can't move: position is not defined" }.add(
            checkNotNull(subject.velocity) { "Can't move: velocity is not defined" }
        )
    }
}
