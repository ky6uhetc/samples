package ru.futurio.game.command

interface Command<T> {
    val subject: T
    fun execute(context: CommandContext)
}
