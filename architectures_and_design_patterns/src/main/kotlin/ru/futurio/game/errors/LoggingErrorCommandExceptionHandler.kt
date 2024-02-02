package ru.futurio.game.errors

import ru.futurio.game.CommandExceptionHandler
import ru.futurio.game.command.Command
import ru.futurio.game.command.LogErrorCommand
import java.util.*
import java.util.function.Consumer

class LoggingErrorCommandExceptionHandler(
    private val logger: Consumer<String>,
    private val retryCommandStrategy: RetryCommandStrategy
) : CommandExceptionHandler<Command<*>, Exception> {
    override val commandClass: Class<Command<*>> = Command::class.java
    override val exceptionClass: Class<Exception> = Exception::class.java

    override fun handleException(exception: Exception, command: Command<*>, commandQueue: Queue<Command<*>>) {
        if (retryCommandStrategy.shouldLog(exception, command)) {
            commandQueue.add(LogErrorCommand(exception, command, logger))
        }
    }
}
