package ru.futurio.command

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import ru.futurio.model.Axis.*
import ru.futurio.model.Positioning
import ru.futurio.model.Rotation
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty.POSITION
import ru.futurio.model.UObjectProperty.VELOCITY_VECTOR
import ru.futurio.model.ability.impl.MovableAdapter
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class MovableTest {

    private val cmdCtx: CommandContext = mock {}

    @Test
    fun `movable adapter`() {
        val velocity = Positioning(-3.0, 0.0)
        val spaceShip = MovableAdapter(
            UObject(VELOCITY_VECTOR to velocity)
        )
        assertTrue(spaceShip.id.isNotBlank())
        assertEquals(velocity, spaceShip.velocity)
    }

    @Test
    fun `horizontal turn`() {
        val spaceShip = MovableAdapter(
            UObject(VELOCITY_VECTOR to Positioning(-3.0, 0.0))
        )
        TurnCommand(spaceShip, Rotation(90.0, Z)).execute(cmdCtx)
        assertEquals(Positioning(-0.0, -3.0), spaceShip.velocity)
        TurnCommand(spaceShip, Rotation(60.0, Z)).execute(cmdCtx)
        assertEquals(Positioning(2.5980762, -1.5), spaceShip.velocity)
    }

    @Test
    fun `vertical turn`() {
        val spaceShip1 = MovableAdapter(
            UObject(VELOCITY_VECTOR to Positioning(-3.0, 0.0))
        )
        TurnCommand(spaceShip1, Rotation(30.0, Y)).execute(cmdCtx)
        assertEquals(Positioning(-2.5980762, 0.0, -1.5), spaceShip1.velocity)
        val spaceShip2 = MovableAdapter(
            UObject(VELOCITY_VECTOR to Positioning(3.0, 2.0))
        )
        TurnCommand(spaceShip2, Rotation(45.0, X)).execute(cmdCtx)
        assertEquals(Positioning(-2.5980762, 0.0, -1.5), spaceShip1.velocity)
    }

    @Test
    fun `can't turn object with undefined velocity`() {
        val spaceShip = MovableAdapter(
            UObject(POSITION to Positioning(12.0, 5.0))
        )
        assertThrows<IllegalStateException> { TurnCommand(spaceShip, Rotation(30.0, Y)).execute(cmdCtx) }.let { ex ->
            assertEquals("Can't turn: velocity is not defined", ex.message)
        }
    }

    @Test
    fun `can't turn object with undefined rotation`() {
        val spaceShip = MovableAdapter(
            UObject(VELOCITY_VECTOR to Positioning(12.0, 5.0))
        )
        assertThrows<IllegalStateException> { TurnCommand(spaceShip, null).execute(cmdCtx) }.let { ex ->
            assertEquals("Can't turn: rotation is not defined", ex.message)
        }
    }
}
