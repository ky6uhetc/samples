package ru.futurio.game.command

import ru.futurio.game.model.ability.Moving

class MoveCommand(
    override val subject: Moving
) : Command<Moving> {
    override fun execute(context: CommandContext) {
        subject.position = checkNotNull(subject.position) { "Can't move: position is not defined" }.add(
            checkNotNull(subject.velocity) { "Can't move: velocity is not defined" }
        )
    }
}
