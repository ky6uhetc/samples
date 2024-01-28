package ru.futurio

import org.junit.jupiter.api.Assertions.assertEquals
import ru.futurio.command.RotateCommand
import ru.futurio.model.Axis.*
import ru.futurio.model.Positioning
import ru.futurio.model.Rotation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.futurio.command.CommandContext
import ru.futurio.model.ability.Movable


class RotateTest {

    private val context = mock<CommandContext> { }

    @Test
    fun `rotate with different axis`() {
        val movable = mock<Movable> {
            whenever(it.velocity).thenAnswer { Positioning(-3.0, 0.0, 1.0) }
        }

        RotateCommand(movable, Rotation(90.0, Z)).execute(context)
        RotateCommand(movable, Rotation(60.0, Z)).execute(context)
        RotateCommand(movable, Rotation(30.0, Z)).execute(context)
        RotateCommand(movable, Rotation(90.0, Y)).execute(context)
        RotateCommand(movable, Rotation(60.0, Y)).execute(context)
        RotateCommand(movable, Rotation(30.0, Y)).execute(context)
        RotateCommand(movable, Rotation(90.0, X)).execute(context)
        RotateCommand(movable, Rotation(60.0, X)).execute(context)
        RotateCommand(movable, Rotation(30.0, X)).execute(context)

        val positionCaptor = argumentCaptor<Positioning>()
        verify(movable, times(9)).velocity = positionCaptor.capture()
        assertEquals(
            listOf(
                Positioning(-0.0, -3.0, 1.0),
                Positioning(-1.5, -2.5980762, 1.0),
                Positioning(-2.5980762, -1.5, 1.0),
                Positioning(1.0, 0.0, 3.0),
                Positioning(-0.6339746, 0.0, 3.0980762),
                Positioning(-2.0980762, 0.0, 2.3660254),
                Positioning(-3.0, -1.0, 0.0),
                Positioning(-3.0, -0.8660254, 0.5),
                Positioning(-3.0, -0.5, 0.8660254)
            ),
            positionCaptor.allValues
        )
    }

    @Test
    fun `can't rotate object with undefined velocity`() {
        val movable = mock<Movable> {
            whenever(it.velocity).thenAnswer { null }
        }

        assertThrows<IllegalStateException> { RotateCommand(movable, Rotation(30.0, Y)).execute(context) }.let { ex ->
            assertEquals("Can't rotate: velocity is not defined", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(movable, times(0)).velocity = positionCaptor.capture()
    }

    @Test
    fun `can't rotate object with erroneous velocity`() {
        val movable = mock<Movable> {
            whenever(it.velocity).thenThrow(RuntimeException("unknown error"))
        }

        assertThrows<RuntimeException> { RotateCommand(movable, Rotation(30.0, Y)).execute(context) }.let { ex ->
            assertEquals("unknown error", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(movable, times(0)).velocity = positionCaptor.capture()
    }

    @Test
    fun `can't rotate object with undefined rotation`() {
        val movable = mock<Movable> {
            whenever(it.velocity).thenAnswer { Positioning(-3.0, 0.0, 1.0) }
        }

        assertThrows<IllegalStateException> { RotateCommand(movable, null).execute(context) }.let { ex ->
            assertEquals("Can't rotate: rotation is not defined", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(movable, times(0)).velocity = positionCaptor.capture()
    }
}
