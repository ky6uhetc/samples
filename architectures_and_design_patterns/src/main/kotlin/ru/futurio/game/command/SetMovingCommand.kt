package ru.futurio.game.command

import ru.futurio.game.model.ability.Movable
import ru.futurio.game.model.Positioning

class SetMovingCommand(
    override val subject: Movable,
    val velocity: Positioning?
) : Command<Movable> {
    override fun execute(context: CommandContext) {
        subject.velocity = velocity
    }
}
