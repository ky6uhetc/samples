package ru.futurio.command

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import ru.futurio.model.Direction
import ru.futurio.model.Positioning
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty.*
import ru.futurio.model.ability.impl.MovingAdapter
import ru.futurio.model.manipulation.impl.MoveCommandEndableAdapter
import java.util.concurrent.LinkedBlockingDeque
import kotlin.test.assertEquals

class StopMoveTest {

    private val objMap: MutableMap<String, UObject?> = mutableMapOf()
    private val cmdCtx: CommandContext = mock { ctx ->
        on(ctx.getUObjectById(any())).thenAnswer { objMap[it.arguments[0]] }
    }

    @BeforeEach
    fun reset() {
        objMap.clear()
    }

    @Test
    fun `stop move`() {
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val commandQueue = LinkedBlockingDeque<Any>().also {
            it.add(MoveCommand(MovingAdapter(spaceShip)))
        }
        val stopMoveOrder = MoveCommandEndableAdapter(
            UObject(
                MANIPULATED_OBJECT_ID to spaceShip.id,
                MOVING_DIRECTION to Direction(30.0, 0.0),
                VELOCITY_MODULUS to 10,
                COMMAND_QUEUE to commandQueue
            )
        )

        StopMoveCommand(stopMoveOrder).execute(cmdCtx)

        MovingAdapter(spaceShip).let {
            assertEquals(Positioning(1.0, 2.0, 0.0), it.position)
            assertNull(it.velocity)
        }
        assertEquals(0, commandQueue.size)
    }

    @Test
    fun `stop move fails due to missing object id`() {
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val commandQueue = LinkedBlockingDeque<Any>().also {
            it.add(MoveCommand(MovingAdapter(spaceShip)))
        }
        val stopMoveOrder = MoveCommandEndableAdapter(
            UObject(
                MOVING_DIRECTION to Direction(30.0, 0.0),
                VELOCITY_MODULUS to 10,
                COMMAND_QUEUE to commandQueue
            )
        )

        assertThrows<IllegalStateException> {
            StopMoveCommand(stopMoveOrder).execute(cmdCtx)
        }.let {
            assertEquals("Manipulated object ID is not defined for stop move command", it.message)
        }
        assertEquals(1, commandQueue.size)
    }

    @Test
    fun `stop move fails due to not found managed object`() {
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val commandQueue = LinkedBlockingDeque<Any>().also {
            it.add(MoveCommand(MovingAdapter(spaceShip)))
        }
        val stopMoveOrder = MoveCommandEndableAdapter(
            UObject(
                MANIPULATED_OBJECT_ID to "wrong id",
                MOVING_DIRECTION to Direction(30.0, 0.0),
                VELOCITY_MODULUS to 10,
                COMMAND_QUEUE to commandQueue
            )
        )

        assertThrows<IllegalArgumentException> {
            StopMoveCommand(stopMoveOrder).execute(cmdCtx)
        }.let {
            assertEquals("Manipulated object not found by ID", it.message)
        }
        assertEquals(1, commandQueue.size)
    }

    @Test
    fun `stop move with missing missing`() {
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val commandQueue = LinkedBlockingDeque<Any>().also {
            it.add(MoveCommand(MovingAdapter(spaceShip)))
        }
        val stopMoveOrder = MoveCommandEndableAdapter(
            UObject(
                MANIPULATED_OBJECT_ID to spaceShip.id,
                MOVING_DIRECTION to Direction(30.0, 0.0),
                VELOCITY_MODULUS to 10
            )
        )

        assertThrows<IllegalStateException> {
            StopMoveCommand(stopMoveOrder).execute(cmdCtx)
        }.let {
            assertEquals("Command queue is not defined for stop move command", it.message)
        }
        assertEquals(1, commandQueue.size)
    }
}
