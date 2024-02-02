package ru.futurio.game.errors

import ru.futurio.game.command.Command
import ru.futurio.game.command.RetryFailedCommandCommand
import java.lang.Exception

class RetryStrategyOnce: RetryCommandStrategy {
    override fun shouldRetry(exception: Exception, command: Command<*>): Boolean {
        return command !is RetryFailedCommandCommand
    }

    override fun shouldLog(exception: Exception, command: Command<*>): Boolean {
        return command is RetryFailedCommandCommand
    }
}
