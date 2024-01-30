package ru.futurio

import org.junit.jupiter.api.Assertions.assertEquals
import ru.futurio.command.RotateCommand
import ru.futurio.model.Axis.*
import ru.futurio.model.Positioning
import ru.futurio.model.Rotation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.futurio.model.Rotatable


class RotateTest {

    @Test
    fun `rotate with different axis`() {
        val rotatable = mock<Rotatable> {
            whenever(it.velocity).thenAnswer { Positioning(-3.0, 0.0, 1.0) }
        }

        RotateCommand(rotatable, Rotation(90.0, Z)).execute()
        RotateCommand(rotatable, Rotation(60.0, Z)).execute()
        RotateCommand(rotatable, Rotation(30.0, Z)).execute()
        RotateCommand(rotatable, Rotation(90.0, Y)).execute()
        RotateCommand(rotatable, Rotation(60.0, Y)).execute()
        RotateCommand(rotatable, Rotation(30.0, Y)).execute()
        RotateCommand(rotatable, Rotation(90.0, X)).execute()
        RotateCommand(rotatable, Rotation(60.0, X)).execute()
        RotateCommand(rotatable, Rotation(30.0, X)).execute()

        val positionCaptor = argumentCaptor<Positioning>()
        verify(rotatable, times(9)).velocity = positionCaptor.capture()
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
        val rotatable = mock<Rotatable> {
            whenever(it.velocity).thenAnswer { null }
        }

        assertThrows<IllegalStateException> { RotateCommand(rotatable, Rotation(30.0, Y)).execute() }.let { ex ->
            assertEquals("Can't rotate: velocity is not defined", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(rotatable, times(0)).velocity = positionCaptor.capture()
    }

    @Test
    fun `can't rotate object with erroneous velocity`() {
        val rotatable = mock<Rotatable> {
            whenever(it.velocity).thenThrow(RuntimeException("unknown error"))
        }

        assertThrows<RuntimeException> { RotateCommand(rotatable, Rotation(30.0, Y)).execute() }.let { ex ->
            assertEquals("unknown error", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(rotatable, times(0)).velocity = positionCaptor.capture()
    }

    @Test
    fun `can't rotate object with undefined rotation`() {
        val rotatable = mock<Rotatable> {
            whenever(it.velocity).thenAnswer { Positioning(-3.0, 0.0, 1.0) }
        }

        assertThrows<IllegalStateException> { RotateCommand(rotatable, null).execute() }.let { ex ->
            assertEquals("Can't rotate: rotation is not defined", ex.message)
        }

        val positionCaptor = argumentCaptor<Positioning>()
        verify(rotatable, times(0)).velocity = positionCaptor.capture()
    }
}
