package ru.futurio

import org.junit.jupiter.api.Test
import ru.futurio.model.Positioning
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty
import ru.futurio.model.UObjectProperty.VELOCITY_VECTOR
import ru.futurio.model.ability.impl.MovableAdapter
import ru.futurio.model.ability.impl.MovingAdapter
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AdaptersTest {

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
    fun `moving adapter`() {
        val position = Positioning(12.0, 5.0)
        val velocity = Positioning(-3.0, 0.0)
        val spaceShip = MovingAdapter(
            UObject(UObjectProperty.POSITION to position, VELOCITY_VECTOR to velocity)
        )
        assertTrue(spaceShip.id.isNotBlank())
        assertEquals(position, spaceShip.position)
        assertEquals(velocity, spaceShip.velocity)
    }
}
