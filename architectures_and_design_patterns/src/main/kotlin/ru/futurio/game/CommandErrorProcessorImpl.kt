package ru.futurio.game

import ru.futurio.game.command.Command
import java.util.*

class CommandErrorProcessorImpl(
    private val commandExceptionHandlers: Collection<CommandExceptionHandler<out Command<*>, out Exception>>
) : CommandErrorProcessor {

    override fun processError(exception: Exception, command: Command<*>, commandQueue: Queue<Command<*>>) {
        commandExceptionHandlers
            .filter { it.exceptionClass.isInstance(exception) && it.commandClass.isInstance(command) }
            .ifEmpty { error("No exception handler found")  }
            .forEach { it.handleException(exception, command, commandQueue) }
    }
}
