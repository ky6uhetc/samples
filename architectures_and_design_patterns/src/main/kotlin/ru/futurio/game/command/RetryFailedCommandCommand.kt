package ru.futurio.game.command

class RetryFailedCommandCommand(
    override val subject: Command<*>,
    val retryNumber: Int = 1
) : Command<Command<*>> {
    override fun execute(context: CommandContext) {
        subject.execute(context)
    }
}
