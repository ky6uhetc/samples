package ru.futurio.game

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.futurio.game.command.MoveCommand
import ru.futurio.game.command.RotateCommand
import ru.futurio.game.command.StartMoveCommand
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

        val errorProcessor = CommandErrorProcessorImpl(listOf(handler1, handler2, handler3))

        errorProcessor.handle(IllegalArgumentException("invalid value"), MoveCommand(mock { }))
        repeat(2) {
            errorProcessor.handle(IllegalStateException("wrong state $it"), MoveCommand(mock { }))
        }
        repeat(3) {
            errorProcessor.handle(IllegalStateException("invalid state $it"), RotateCommand(mock { }, null))
        }

        val exceptionCaptor = argumentCaptor<Exception>()
        verify(handler1, times(1)).handleException(exceptionCaptor.capture())
        verify(handler2, times(2)).handleException(exceptionCaptor.capture())
        verify(handler3, times(3)).handleException(exceptionCaptor.capture())
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

        val errorProcessor = CommandErrorProcessorImpl(listOf(handler1, handler2, handler3))

        assertThrows<NotImplementedError> {
            errorProcessor.handle(IllegalArgumentException("invalid value"), StartMoveCommand(mock { }))
        }
    }
}
