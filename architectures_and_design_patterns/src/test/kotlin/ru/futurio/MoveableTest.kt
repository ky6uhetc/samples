package ru.futurio

import org.example.ru.futurio.command.Move
import org.example.ru.futurio.model.Positioning
import org.example.ru.futurio.model.impl.SpaceShip
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class MoveableTest {

    @Test
    fun `linear motion`() {
        val spaceShip = SpaceShip(position = Positioning(12.0, 5.0), velocity = Positioning(-7.0, 3.0))
        Move(spaceShip).execute()
        assertEquals(Positioning(5.0, 8.0), spaceShip.position)
    }

    @Test
    fun `can't move object with undefined position`() {
        val spaceShip = SpaceShip(position = null, velocity = Positioning(-7.0, 3.0))
        assertThrows<IllegalStateException> {  Move(spaceShip).execute() }.let { ex ->
            assertEquals("Position is not defined", ex.message)
        }
    }

    @Test
    fun `can't move object with undefined velocity`() {
        val spaceShip = SpaceShip(position = Positioning(12.0, 5.0), velocity = null)
        assertThrows<IllegalStateException> {  Move(spaceShip).execute() }.let { ex ->
            assertEquals("Velocity is not defined", ex.message)
        }
    }
}
