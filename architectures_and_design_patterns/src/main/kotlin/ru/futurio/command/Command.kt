package ru.futurio.command

interface Command<T> {
    val subject: T
    fun execute(context: CommandContext)
}
