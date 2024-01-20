package org.example.ru.futurio.command

import org.example.ru.futurio.model.Rotation
import org.example.ru.futurio.model.Turnable

class Turn(
    override val subject: Turnable,
    val rotation: Rotation?
): ActionCommand<Turnable> {
    override fun execute() {
        TODO("Not implemented yet")
    }
}
