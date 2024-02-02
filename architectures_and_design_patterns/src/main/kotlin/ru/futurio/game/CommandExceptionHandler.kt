package ru.futurio.game

import ru.futurio.game.command.Command
import java.util.*

interface CommandExceptionHandler<T : Command<*>, E : Exception> {

    val commandClass: Class<T>
    val exceptionClass: Class<E>
    fun handleException(exception: Exception,  command: Command<*>, commandQueue: Queue<Command<*>>)
}
