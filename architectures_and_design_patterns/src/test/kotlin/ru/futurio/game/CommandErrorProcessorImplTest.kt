package ru.futurio.game

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.futurio.game.command.Command
import ru.futurio.game.command.MoveCommand
import ru.futurio.game.command.RotateCommand
import ru.futurio.game.command.StartMoveCommand
import java.util.concurrent.LinkedBlockingQueue
import kotlin.test.assertEquals

class CommandErrorProcessorImplTest {

    private val handler1: CommandExceptionHandler<MoveCommand, IllegalArgumentException> = mock {
        whenever(it.commandClass).thenReturn(MoveCommand::class.java)
        whenever(it.exceptionClass).thenReturn(IllegalArgumentException::class.java)
    }
    private val handler2: CommandExceptionHandler<MoveCommand, IllegalStateException> = mock {
        whenever(it.commandClass).thenReturn(MoveCommand::class.java)
        whenever(it.exceptionClass).thenReturn(IllegalStateException::class.java)
    }
    private val handler3: CommandExceptionHandler<RotateCommand, IllegalStateException> = mock {
        whenever(it.commandClass).thenReturn(RotateCommand::class.java)
        whenever(it.exceptionClass).thenReturn(IllegalStateException::class.java)
    }

    @Test
    fun `exception handler is invoked based on exception and command`() {

        val commandQueue = LinkedBlockingQueue<Command<*>>()
        val errorProcessor = CommandErrorProcessorImpl(listOf(handler1, handler2, handler3))

        errorProcessor.processError(IllegalArgumentException("invalid value"), MoveCommand(mock { }), commandQueue)
        repeat(2) {
            errorProcessor.processError(IllegalStateException("wrong state $it"), MoveCommand(mock { }), commandQueue)
        }
        repeat(3) {
            errorProcessor.processError(
                IllegalStateException("invalid state $it"),
                RotateCommand(mock { }, null),
                commandQueue
            )
        }

        val exceptionCaptor = argumentCaptor<Exception>()
        verify(handler1, times(1)).handleException(exceptionCaptor.capture(), any(), any())
        verify(handler2, times(2)).handleException(exceptionCaptor.capture(), any(), any())
        verify(handler3, times(3)).handleException(exceptionCaptor.capture(), any(), any())
        assertEquals(
            listOf(
                "invalid value",
                "wrong state 0",
                "wrong state 1",
                "invalid state 0",
                "invalid state 1",
                "invalid state 2"
            ),
            exceptionCaptor.allValues.map { it.message }
        )
    }

    @Test
    fun `exception handler not found based on exception and command`() {

        val commandQueue = LinkedBlockingQueue<Command<*>>()
        val errorProcessor = CommandErrorProcessorImpl(listOf(handler1, handler2, handler3))

        assertThrows<IllegalStateException> {
            errorProcessor.processError(IllegalArgumentException("invalid value"), StartMoveCommand(mock { }), commandQueue)
        }.also {
            assertEquals("No exception handler found", it.message)
        }
    }
}
