package ru.futurio.game

import ru.futurio.game.command.Command

class CommandErrorProcessorImpl(
    commandExceptionHandlers: Collection<CommandExceptionHandler<out Command<*>, out Exception>>
) : CommandErrorProcessor {

    private val commandClassToExceptionHandler = commandExceptionHandlers.groupBy { it.commandClass }

    override fun handle(exception: Exception, command: Command<*>) {
        commandClassToExceptionHandler[command.javaClass]
            ?.find { it.exceptionClass.isInstance(exception) }
            ?.handleException(exception)
            ?: run {
                TODO("Generic exception handling is not implemented yet")
            }
    }
}
