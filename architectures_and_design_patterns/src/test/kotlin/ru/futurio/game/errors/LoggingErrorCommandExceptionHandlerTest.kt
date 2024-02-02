package ru.futurio.game.errors

import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.futurio.game.command.Command
import ru.futurio.game.command.LogErrorCommand
import ru.futurio.game.command.RetryFailedCommandCommand
import java.util.*
import java.util.function.Consumer
import kotlin.test.assertEquals

class LoggingErrorCommandExceptionHandlerTest {

    @Test
    fun `handle command exception when shouldn't log`() {
        val logger: Consumer<String> = mock { }
        val command: Command<*> = mock { }
        val commandQueue: Queue<Command<*>> = mock { }
        val retryStrategy = mock<RetryCommandStrategy> {
            whenever(it.shouldLog(any(), any())).thenReturn(false)
        }
        val handler = LoggingErrorCommandExceptionHandler(logger, retryStrategy)
        val exception = IllegalArgumentException("wrong value")

        handler.handleException(exception, command, commandQueue)

        verify(retryStrategy, times(1)).shouldLog(exception, command)
        verifyNoMoreInteractions(retryStrategy)
        verifyNoInteractions(commandQueue)
    }

    @Test
    fun `handle command exception when should log`() {
        val logger: Consumer<String> = mock { }
        val command: Command<*> = RetryFailedCommandCommand(mock { })
        val commandQueue: Queue<Command<*>> = mock { }
        val retryStrategy = mock<RetryCommandStrategy> {
            whenever(it.shouldLog(any(), any())).thenReturn(true)
        }
        val handler = LoggingErrorCommandExceptionHandler(logger, retryStrategy)
        val exception = IllegalArgumentException("wrong value")

        handler.handleException(exception, command, commandQueue)

        verify(retryStrategy, times(1)).shouldLog(exception, command)
        verifyNoMoreInteractions(retryStrategy)
        val commandCaptor = argumentCaptor<Command<*>>()
        verify(commandQueue, times(1)).add(commandCaptor.capture())
        assertInstanceOf(LogErrorCommand::class.java, commandCaptor.firstValue)
    }

    @Test
    fun `adding to queue fails`() {
        val logger: Consumer<String> = mock { }
        val command: Command<*> = RetryFailedCommandCommand(mock { })
        val commandQueue: Queue<Command<*>> = mock {
            whenever(it.add(any())).thenThrow(RuntimeException("something went wrong"))
        }
        val retryStrategy = mock<RetryCommandStrategy> {
            whenever(it.shouldLog(any(), any())).thenReturn(true)
        }
        val handler = LoggingErrorCommandExceptionHandler(logger, retryStrategy)
        val exception = IllegalArgumentException("wrong value")

        assertThrows<RuntimeException> {
            handler.handleException(exception, command, commandQueue)
        }.also {
            assertEquals("something went wrong", it.message)
        }

        verify(retryStrategy, times(1)).shouldLog(exception, command)
        verifyNoMoreInteractions(retryStrategy)
        val commandCaptor = argumentCaptor<Command<*>>()
        verify(commandQueue, times(1)).add(commandCaptor.capture())
        assertInstanceOf(LogErrorCommand::class.java, commandCaptor.firstValue)
    }

    @Test
    fun `retry strategy fails`() {
        val logger: Consumer<String> = mock { }
        val command: Command<*> = RetryFailedCommandCommand(mock { })
        val commandQueue: Queue<Command<*>> = mock {}
        val retryStrategy = mock<RetryCommandStrategy> {
            whenever(it.shouldLog(any(), any())).thenThrow(RuntimeException("OMG"))
        }
        val handler = LoggingErrorCommandExceptionHandler(logger, retryStrategy)
        val exception = IllegalArgumentException("wrong value")

        assertThrows<RuntimeException> {
            handler.handleException(exception, command, commandQueue)
        }.also {
            assertEquals("OMG", it.message)
        }

        verify(retryStrategy, times(1)).shouldLog(exception, command)
        verifyNoMoreInteractions(retryStrategy)
        verifyNoInteractions(commandQueue)
    }
}
