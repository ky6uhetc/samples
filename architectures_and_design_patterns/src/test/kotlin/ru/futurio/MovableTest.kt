package ru.futurio

import org.example.ru.futurio.command.MoveCommand
import org.example.ru.futurio.model.Positioning
import org.example.ru.futurio.model.impl.SpaceShip
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class MovableTest {

    @Test
    fun `linear motion`() {
        val spaceShip = SpaceShip(position = Positioning(12.0, 5.0), velocity = Positioning(-7.0, 3.0))
        MoveCommand(spaceShip).execute()
        assertEquals(Positioning(5.0, 8.0), spaceShip.position)
    }

    @Test
    fun `can't move object with undefined position`() {
        val spaceShip = SpaceShip(position = null, velocity = Positioning(-7.0, 3.0))
        assertThrows<IllegalStateException> {  MoveCommand(spaceShip).execute() }.let { ex ->
            assertEquals("Can't move: position is not defined", ex.message)
        }
    }

    @Test
    fun `can't move object with undefined velocity`() {
        val spaceShip = SpaceShip(position = Positioning(12.0, 5.0), velocity = null)
        assertThrows<IllegalStateException> {  MoveCommand(spaceShip).execute() }.let { ex ->
            assertEquals("Can't move: velocity is not defined", ex.message)
        }
    }
}
