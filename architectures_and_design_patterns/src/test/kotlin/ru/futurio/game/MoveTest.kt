package ru.futurio.game

import org.junit.jupiter.api.Assertions.assertEquals
import ru.futurio.game.command.MoveCommand
import ru.futurio.game.model.Positioning
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.futurio.game.command.CommandContext
import ru.futurio.game.model.ability.Moving

class MoveTest {

    private val context = mock<CommandContext> { }

    @Test
    fun `linear motion`() {
        val movable = mock<Moving> {
            whenever(it.position).thenAnswer { Positioning(12.0, 5.0) }
            whenever(it.velocity).thenAnswer { Positioning(-7.0, 3.0) }
        }

        MoveCommand(movable).execute(context)

        val positionCaptor = argumentCaptor<Positioning>()
        verify(movable, times(1)).position = positionCaptor.capture()
        assertEquals(listOf(Positioning(5.0, 8.0)), positionCaptor.allValues)
    }

    @Test
    fun `can't move object with undefined position`() {
        val movable = mock<Moving> {
            whenever(it.position).thenAnswer { null }
            whenever(it.velocity).thenAnswer { Positioning(-7.0, 3.0) }
        }

        assertThrows<IllegalStateException> { MoveCommand(movable).execute(context) }.let { ex ->
            assertEquals("Can't move: position is not defined", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(movable, times(0)).position = positionCaptor.capture()
    }

    @Test
    fun `can't move object with erroneous position`() {
        val movable = mock<Moving> {
            whenever(it.position).thenThrow(RuntimeException("unknown error"))
            whenever(it.velocity).thenAnswer { Positioning(-7.0, 3.0) }
        }
        assertThrows<RuntimeException> { MoveCommand(movable).execute(context) }.let { ex ->
            assertEquals("unknown error", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(movable, times(0)).position = positionCaptor.capture()
    }

    @Test
    fun `can't move object with undefined velocity`() {
        val movable = mock<Moving> {
            whenever(it.position).thenAnswer { Positioning(12.0, 5.0) }
            whenever(it.velocity).thenAnswer { null }
        }

        assertThrows<IllegalStateException> { MoveCommand(movable).execute(context) }.let { ex ->
            assertEquals("Can't move: velocity is not defined", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(movable, times(0)).position = positionCaptor.capture()
    }

    @Test
    fun `can't move object with erroneous velocity`() {
        val movable = mock<Moving> {
            whenever(it.position).thenAnswer { Positioning(12.0, 5.0) }
            whenever(it.velocity).thenThrow(RuntimeException("unknown error"))
        }

        assertThrows<RuntimeException> { MoveCommand(movable).execute(context) }.let { ex ->
            assertEquals("unknown error", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(movable, times(0)).position = positionCaptor.capture()
    }
}
