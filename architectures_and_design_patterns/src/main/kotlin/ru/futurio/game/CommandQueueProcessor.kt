package ru.futurio.game

import ru.futurio.game.command.Command
import ru.futurio.game.command.CommandContext
import java.util.concurrent.LinkedBlockingQueue

class CommandQueueProcessor(
    private val commandErrorProcessor: CommandErrorProcessor
) {

    fun process(commandContext: CommandContext, commandQueue: LinkedBlockingQueue<Command<*>?>) {
        var interrupted = false
        while (commandQueue.isNotEmpty() && interrupted.not()) {
            commandQueue.poll()?.also { command ->
                try {
                    command.execute(commandContext)
                } catch (ex: Exception) {
                    if (ex is InterruptedException) {
                        interrupted = true
                    }
                    commandErrorProcessor.handle(ex, command)
                }
            }
        }
    }
}
