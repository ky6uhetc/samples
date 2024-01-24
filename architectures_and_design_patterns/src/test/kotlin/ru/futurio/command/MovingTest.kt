package ru.futurio.command

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import ru.futurio.model.Positioning
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty.POSITION
import ru.futurio.model.UObjectProperty.VELOCITY_VECTOR
import ru.futurio.model.ability.impl.MovingAdapter
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MovingTest {

    private val cmdCtx: CommandContext = mock {}

    @Test
    fun `moving adapter`() {
        val position = Positioning(12.0, 5.0)
        val velocity = Positioning(-3.0, 0.0)
        val spaceShip = MovingAdapter(
            UObject(POSITION to position, VELOCITY_VECTOR to velocity)
        )
        assertTrue(spaceShip.id.isNotBlank())
        assertEquals(position, spaceShip.position)
        assertEquals(velocity, spaceShip.velocity)
    }

    @Test
    fun `linear motion`() {
        val spaceShip = MovingAdapter(
            UObject(POSITION to Positioning(12.0, 5.0), VELOCITY_VECTOR to Positioning(-7.0, 3.0))
        )
        MoveCommand(spaceShip).execute(cmdCtx)
        assertEquals(Positioning(5.0, 8.0), spaceShip.position)
    }

    @Test
    fun `can't move object with undefined position`() {
        val spaceShip = MovingAdapter(
            UObject(VELOCITY_VECTOR to Positioning(-7.0, 3.0))
        )
        assertThrows<IllegalStateException> { MoveCommand(spaceShip).execute(cmdCtx) }.let { ex ->
            assertEquals("Can't move: position is not defined", ex.message)
        }
    }

    @Test
    fun `can't move object with undefined velocity`() {
        val spaceShip = MovingAdapter(
            UObject(POSITION to Positioning(12.0, 5.0))
        )
        assertThrows<IllegalStateException> { MoveCommand(spaceShip).execute(cmdCtx) }.let { ex ->
            assertEquals("Can't move: velocity is not defined", ex.message)
        }
    }
}
