package ru.futurio.game.errors

import ru.futurio.game.command.Command
import java.lang.Exception

interface RetryCommandStrategy {
    fun shouldRetry(exception: Exception, command: Command<*>): Boolean
    fun shouldLog(exception: Exception, command: Command<*>): Boolean
}
