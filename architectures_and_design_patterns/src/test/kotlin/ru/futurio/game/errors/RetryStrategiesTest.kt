package ru.futurio.game.errors

import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import ru.futurio.game.CommandErrorProcessorImpl
import ru.futurio.game.command.Command
import ru.futurio.game.command.LogErrorCommand
import ru.futurio.game.command.RetryFailedCommandCommand
import java.util.*
import java.util.function.Consumer
import kotlin.test.assertEquals

class RetryStrategiesTest {

    @Test
    fun `no retrying`() {
        val strategy = RetryStrategyNoRetries()
        val commandQueue = mock<Queue<Command<*>>> { }
        val errorProcessor = CommandErrorProcessorImpl(
            listOf(
                LoggingErrorCommandExceptionHandler(mock { }, strategy),
                RetryCommandExceptionHandler(strategy)
            )
        )

        errorProcessor.processError(RuntimeException("error"), mock { }, commandQueue)

        val commandCaptor = argumentCaptor<Command<*>>()
        verify(commandQueue, times(1)).add(commandCaptor.capture())
        assertEquals(
            listOf(LogErrorCommand::class.java),
            commandCaptor.allValues.map { it::class.java }
        )
    }

    @Test
    fun `retry once`() {
        val strategy = RetryStrategyOnce()
        val logger = mock<Consumer<String>> { }
        val command = mock<Command<*>> { }
        val commandQueue = mock<Queue<Command<*>>> { }
        val errorProcessor = CommandErrorProcessorImpl(
            listOf(
                LoggingErrorCommandExceptionHandler(logger, strategy),
                RetryCommandExceptionHandler(strategy)
            )
        )

        errorProcessor.processError(RuntimeException("error"), command, commandQueue)
        errorProcessor.processError(RuntimeException("error"), RetryFailedCommandCommand(command), commandQueue)

        val commandCaptor = argumentCaptor<Command<*>>()
        verify(commandQueue, times(2)).add(commandCaptor.capture())
        assertEquals(
            listOf(RetryFailedCommandCommand::class.java, LogErrorCommand::class.java),
            commandCaptor.allValues.map { it::class.java }
        )
    }

    @Test
    fun `retry twice`() {
        val strategy = RetryStrategyTwice()
        val logger = mock<Consumer<String>> { }
        val command = mock<Command<*>> { }
        val commandQueue = mock<Queue<Command<*>>> { }
        val errorProcessor = CommandErrorProcessorImpl(
            listOf(
                LoggingErrorCommandExceptionHandler(logger, strategy),
                RetryCommandExceptionHandler(strategy)
            )
        )

        errorProcessor.processError(RuntimeException("error"), command, commandQueue)
        errorProcessor.processError(RuntimeException("error"), RetryFailedCommandCommand(command), commandQueue)
        errorProcessor.processError(RuntimeException("error"), RetryFailedCommandCommand(command, 2), commandQueue)

        val commandCaptor = argumentCaptor<Command<*>>()
        verify(commandQueue, times(3)).add(commandCaptor.capture())
        assertEquals(
            listOf(
                RetryFailedCommandCommand::class.java,
                RetryFailedCommandCommand::class.java,
                LogErrorCommand::class.java
            ),
            commandCaptor.allValues.map { it::class.java }
        )
    }
}
