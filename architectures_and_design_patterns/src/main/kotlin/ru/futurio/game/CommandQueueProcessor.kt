package ru.futurio.game

import ru.futurio.game.command.Command
import ru.futurio.game.command.CommandContext
import java.util.*

class CommandQueueProcessor(
    private val commandErrorProcessor: CommandErrorProcessor
) {

    fun process(commandContext: CommandContext, commandQueue: Queue<Command<*>>) {
        var interrupted = false
        while (commandQueue.isNotEmpty() && interrupted.not()) {
            commandQueue.poll()?.also { command ->
                try {
                    command.execute(commandContext)
                } catch (ex: Exception) {
                    if (ex is InterruptedException) {
                        interrupted = true
                    }
                    commandErrorProcessor.processError(ex, command, commandQueue)
                }
            }
        }
    }
}
