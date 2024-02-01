package ru.futurio.game

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.futurio.game.command.Command
import ru.futurio.game.command.CommandContext
import ru.futurio.game.command.RetryFailedCommandCommand
import kotlin.test.assertEquals

class RetryFailedCommandTest {

    @Test
    fun `retry command happy path`() {
        val context = mock<CommandContext> { }
        val failed = mock<Command<*>> { }
        val command = RetryFailedCommandCommand(failed)

        command.execute(context)

        verify(failed, times(1)).execute(context)
    }

    @Test
    fun `retry command failure`() {
        val context = mock<CommandContext> { }
        val failed = mock<Command<*>> {
            whenever(it.execute(context)).thenThrow(RuntimeException("unexpected error"))
        }
        val command = RetryFailedCommandCommand(failed)

        assertThrows<RuntimeException> {
            command.execute(context)
        }.also {
            assertEquals("unexpected error", it.message)
        }
    }
}
