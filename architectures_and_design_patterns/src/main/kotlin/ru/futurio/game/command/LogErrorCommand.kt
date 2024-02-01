package ru.futurio.game.command

import java.util.function.Consumer

class LogErrorCommand(
    override val subject: Exception,
    private val logger: Consumer<String>
) : Command<Exception> {
    override fun execute(context: CommandContext) {
        logger.accept("Error occurred: ${subject.message}")
    }
}
