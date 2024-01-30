package ru.futurio.game

import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import ru.futurio.game.command.SetMovingCommand
import ru.futurio.game.model.Positioning
import ru.futurio.game.model.UObject
import ru.futurio.game.model.UObjectProperty
import ru.futurio.game.model.UObjectProperty.VELOCITY_VECTOR
import ru.futurio.game.model.ability.impl.MovableAdapter
import kotlin.test.assertEquals

class SetMovingTest {

    @Test
    fun `set moving`() {
        val spaceShip = UObject(UObjectProperty.POSITION to Positioning(1.0, 2.0, 0.0))
        val velocity = Positioning(3.0, 0.0, 0.0)
        val command = SetMovingCommand(MovableAdapter(spaceShip), velocity).also {
            it.execute(mock { })
        }
        assertEquals(velocity, spaceShip.getProperty(VELOCITY_VECTOR))
        assertEquals(velocity, command.velocity)
    }
}
