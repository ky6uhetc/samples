package ru.futurio.game.command

class NoOpCommand<T>(override val subject: T) : InternalCommand<T> {
    override fun execute(context: CommandContext) {
        // Nothing to do
    }
}
