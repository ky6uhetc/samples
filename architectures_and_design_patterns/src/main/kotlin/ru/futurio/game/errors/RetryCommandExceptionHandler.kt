package ru.futurio.game.errors

import ru.futurio.game.CommandExceptionHandler
import ru.futurio.game.command.Command
import ru.futurio.game.command.RetryFailedCommandCommand
import java.util.*

class RetryCommandExceptionHandler(
    private val retryCommandStrategy: RetryCommandStrategy
) : CommandExceptionHandler<Command<*>, Exception> {
    override val commandClass: Class<Command<*>> = Command::class.java
    override val exceptionClass: Class<Exception> = Exception::class.java

    override fun handleException(exception: Exception, command: Command<*>, commandQueue: Queue<Command<*>>) {
        if (retryCommandStrategy.shouldRetry(exception, command)) {
            if (command is RetryFailedCommandCommand) {
                commandQueue.add(RetryFailedCommandCommand(command.subject, command.retryNumber.inc()))
            } else {
                commandQueue.add(RetryFailedCommandCommand(command))
            }
        }
    }
}
