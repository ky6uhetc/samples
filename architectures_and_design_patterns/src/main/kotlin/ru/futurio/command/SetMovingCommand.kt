package ru.futurio.command

import ru.futurio.model.ability.Movable
import ru.futurio.model.Positioning

class SetMovingCommand(
    override val subject: Movable,
    val velocity: Positioning?
) : Command<Movable> {
    override fun execute(context: CommandContext) {
        subject.velocity = velocity
    }
}
