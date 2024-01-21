package org.example.ru.futurio.command

import org.example.ru.futurio.model.Moveable

class Move(
    override val subject: Moveable
) : ActionCommand<Moveable> {
    override fun execute() {
        subject.position = checkNotNull(subject.position) { "Can't move: position is not defined" }.add(
            checkNotNull(subject.velocity) { "Can't move: velocity is not defined" }
        )
    }
}
