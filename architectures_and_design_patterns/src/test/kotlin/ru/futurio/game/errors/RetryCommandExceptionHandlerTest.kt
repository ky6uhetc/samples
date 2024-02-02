package ru.futurio.game.errors

import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.futurio.game.command.Command
import ru.futurio.game.command.RetryFailedCommandCommand
import java.util.*
import kotlin.test.assertEquals

class RetryCommandExceptionHandlerTest {

    @Test
    fun `handle command exception`() {
        val command: Command<*> = mock { }
        val commandQueue: Queue<Command<*>> = mock { }
        val retryStrategy: RetryCommandStrategy = mock { whenever(it.shouldRetry(any(), any())).thenReturn(true) }
        val handler = RetryCommandExceptionHandler(retryStrategy)
        val exception = IllegalArgumentException("wrong value")

        handler.handleException(exception, command, commandQueue)

        verify(retryStrategy, times(1)).shouldRetry(exception, command)
        verifyNoMoreInteractions(retryStrategy)
        val commandCaptor = argumentCaptor<Command<*>>()
        verify(commandQueue, times(1)).add(commandCaptor.capture())
        assertInstanceOf(RetryFailedCommandCommand::class.java, commandCaptor.firstValue)
        assertEquals(command, (commandCaptor.firstValue as RetryFailedCommandCommand).subject)
        (commandCaptor.firstValue as RetryFailedCommandCommand).also {
            assertEquals(command, it.subject)
            assertEquals(1, it.retryNumber)
        }
    }

    @Test
    fun `handle command exception when shouldn't retry`() {
        val command: Command<*> = RetryFailedCommandCommand(mock { })
        val commandQueue: Queue<Command<*>> = mock { }
        val retryStrategy: RetryCommandStrategy = mock { whenever(it.shouldRetry(any(), any())).thenReturn(false) }
        val handler = RetryCommandExceptionHandler(retryStrategy)
        val exception = IllegalArgumentException("wrong value")

        handler.handleException(exception, command, commandQueue)

        verify(retryStrategy, times(1)).shouldRetry(exception, command)
        verifyNoMoreInteractions(retryStrategy)
        verifyNoInteractions(commandQueue)
    }

    @Test
    fun `handle retry failed command exception`() {
        val retryCommand: Command<*> = RetryFailedCommandCommand(mock { })
        val commandQueue: Queue<Command<*>> = mock { }
        val retryStrategy: RetryCommandStrategy = mock { whenever(it.shouldRetry(any(), any())).thenReturn(true) }
        val handler = RetryCommandExceptionHandler(retryStrategy)
        val exception = IllegalArgumentException("wrong value")

        handler.handleException(exception, retryCommand, commandQueue)

        verify(retryStrategy, times(1)).shouldRetry(exception, retryCommand)
        verifyNoMoreInteractions(retryStrategy)
        val commandCaptor = argumentCaptor<Command<*>>()
        verify(commandQueue, times(1)).add(commandCaptor.capture())
        assertInstanceOf(RetryFailedCommandCommand::class.java, commandCaptor.firstValue)
        (commandCaptor.firstValue as RetryFailedCommandCommand).also {
            assertEquals(retryCommand.subject, it.subject)
            assertEquals(2, it.retryNumber)
        }
    }

    @Test
    fun `retry strategy fails`() {
        val command: Command<*> = mock { }
        val commandQueue: Queue<Command<*>> = mock { }
        val retryStrategy: RetryCommandStrategy = mock {
            whenever(it.shouldRetry(any(), any())).thenThrow(RuntimeException("something went wrong"))
        }
        val handler = RetryCommandExceptionHandler(retryStrategy)
        val exception = IllegalArgumentException("wrong value")

        assertThrows<RuntimeException> {
            handler.handleException(exception, command, commandQueue)
        }.also {
            assertEquals("something went wrong", it.message)
        }
        verify(retryStrategy, times(1)).shouldRetry(exception, command)
        verifyNoMoreInteractions(retryStrategy)
        verifyNoInteractions(commandQueue)
    }

    @Test
    fun `adding to queue fails`() {
        val command: Command<*> = mock { }
        val commandQueue: Queue<Command<*>> = mock {
            whenever(it.add(any())).thenThrow(RuntimeException("something went wrong"))
        }
        val retryStrategy: RetryCommandStrategy = mock { whenever(it.shouldRetry(any(), any())).thenReturn(true) }
        val handler = RetryCommandExceptionHandler(retryStrategy)
        val exception = IllegalArgumentException("wrong value")

        assertThrows<RuntimeException> {
            handler.handleException(exception, command, commandQueue)
        }.also {
            assertEquals("something went wrong", it.message)
        }
        verify(retryStrategy, times(1)).shouldRetry(exception, command)
        verifyNoMoreInteractions(retryStrategy)
        verify(commandQueue, times(1)).add(any())
    }
}
