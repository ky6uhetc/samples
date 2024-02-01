package ru.futurio.game

import ru.futurio.game.command.Command

interface CommandErrorProcessor {
    fun handle(exception: Exception, command: Command<*>)
}
