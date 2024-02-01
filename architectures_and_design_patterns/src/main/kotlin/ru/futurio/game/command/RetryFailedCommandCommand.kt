package ru.futurio.game.command

class RetryFailedCommandCommand(
    override val subject: Command<*>
) : Command<Command<*>> {
    override fun execute(context: CommandContext) {
        subject.execute(context)
    }
}
