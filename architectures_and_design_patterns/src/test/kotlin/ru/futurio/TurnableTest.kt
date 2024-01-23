package ru.futurio

import org.example.ru.futurio.command.TurnCommand
import org.example.ru.futurio.model.Axis.*
import org.example.ru.futurio.model.Positioning
import org.example.ru.futurio.model.Rotation
import org.example.ru.futurio.model.impl.SpaceShip
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals


class TurnableTest {

    @Test
    fun `horizontal turn`() {
        val spaceShip = SpaceShip(position = Positioning(12.0, 5.0), velocity = Positioning(-3.0, 0.0))
        TurnCommand(spaceShip, Rotation(90.0, Z)).execute()
        assertEquals(Positioning(-0.0, -3.0), spaceShip.velocity)
        TurnCommand(spaceShip, Rotation(60.0, Z)).execute()
        assertEquals(Positioning(2.598076211353317, -1.5), spaceShip.velocity)
    }

    @Test
    fun `vertical turn`() {
        val spaceShip1 = SpaceShip(position = Positioning(12.0, 5.0), velocity = Positioning(-3.0, 0.0))
        TurnCommand(spaceShip1, Rotation(30.0, Y)).execute()
        assertEquals(Positioning(-2.598076211353317, 0.0, -1.5), spaceShip1.velocity)
        val spaceShip2 = SpaceShip(position = Positioning(12.0, 5.0), velocity = Positioning(3.0, 2.0))
        TurnCommand(spaceShip2, Rotation(45.0, X)).execute()
        assertEquals(Positioning(-2.598076211353317, 0.0, -1.5), spaceShip1.velocity)
    }

    @Test
    fun `can't turn object with undefined velocity`() {
        val spaceShip = SpaceShip(position = Positioning(12.0, 5.0), velocity = null)
        assertThrows<IllegalStateException> {  TurnCommand(spaceShip, Rotation(30.0, Y)).execute() }.let { ex ->
            assertEquals("Can't turn: velocity is not defined", ex.message)
        }
    }

    @Test
    fun `can't turn object with undefined rotation`() {
        val spaceShip = SpaceShip(position = Positioning(12.0, 5.0), velocity = Positioning(12.0, 5.0))
        assertThrows<IllegalStateException> {  TurnCommand(spaceShip, null).execute() }.let { ex ->
            assertEquals("Can't turn: rotation is not defined", ex.message)
        }
    }
}
