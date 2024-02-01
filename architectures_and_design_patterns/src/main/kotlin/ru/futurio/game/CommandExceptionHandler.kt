package ru.futurio.game

import ru.futurio.game.command.Command
import java.lang.Exception

interface CommandExceptionHandler<T : Command<*>, E : Exception> {

    val commandClass: Class<T>
    val exceptionClass: Class<E>
    fun handleException(exception: Exception)
}
