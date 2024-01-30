package ru.futurio.command

interface ActionCommand<T> {
    val subject: T
    fun execute()
}
