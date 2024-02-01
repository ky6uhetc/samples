package ru.futurio.game

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.futurio.game.command.LogErrorCommand
import java.util.function.Consumer
import kotlin.test.assertEquals

class LogErrorTest {

    @Test
    fun `log command happy path`() {
        val logger = mock<Consumer<String>> { }
        val command = LogErrorCommand(IllegalArgumentException("wrong value"), logger)

        command.execute(mock { })

        val stringCaptor = argumentCaptor<String>()
        verify(logger, times(1)).accept(stringCaptor.capture())
        assertEquals("Error occurred: wrong value", stringCaptor.firstValue)
    }

    @Test
    fun `logger failure test`() {
        val logger = mock<Consumer<String>> {
            whenever(it.accept(any())).thenThrow(RuntimeException("unexpected error"))
        }
        val command = LogErrorCommand(IllegalArgumentException("wrong value"), logger)

        assertThrows<RuntimeException> {
            command.execute(mock { })
        }.also {
            assertEquals("unexpected error", it.message)
        }
    }
}
