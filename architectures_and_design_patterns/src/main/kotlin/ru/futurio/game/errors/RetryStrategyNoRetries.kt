package ru.futurio.game.errors

import ru.futurio.game.command.Command
import java.lang.Exception

class RetryStrategyNoRetries : RetryCommandStrategy {
    override fun shouldRetry(exception: Exception, command: Command<*>): Boolean {
        return false
    }

    override fun shouldLog(exception: Exception, command: Command<*>): Boolean {
        return true
    }
}
