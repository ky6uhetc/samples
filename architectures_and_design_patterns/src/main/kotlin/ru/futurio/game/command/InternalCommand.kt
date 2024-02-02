package ru.futurio.game.command

interface InternalCommand<T> {
    val subject: T
    fun execute(context: CommandContext)
}
