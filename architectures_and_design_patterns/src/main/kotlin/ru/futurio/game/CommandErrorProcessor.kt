package ru.futurio.game

import ru.futurio.game.command.Command
import java.util.*

interface CommandErrorProcessor {
    fun processError(exception: Exception, command: Command<*>, commandQueue: Queue<Command<*>>)
}
