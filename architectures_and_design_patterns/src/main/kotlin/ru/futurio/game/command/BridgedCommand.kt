package ru.futurio.game.command

class BridgedCommand<T>(
    override val subject: T,
    private var injectedCommand: InternalCommand<*>
) : Command<T> {

    @Suppress("unchecked_cast")
    val command: InternalCommand<T> = injectedCommand as InternalCommand<T>

    constructor(command: InternalCommand<T>) : this(command.subject, command)

    override fun execute(context: CommandContext) {
        injectedCommand.execute(context)
    }

    fun inject(replaceWith: InternalCommand<*>) {
        injectedCommand = replaceWith
    }
}
