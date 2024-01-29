package ru.futurio.game

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import ru.futurio.game.command.Command
import ru.futurio.game.command.CommandContext
import ru.futurio.game.command.StartMoveCommand
import ru.futurio.game.model.Direction
import ru.futurio.game.model.Positioning
import ru.futurio.game.model.UObject
import ru.futurio.game.model.UObjectProperty.*
import ru.futurio.game.model.ability.impl.MovingAdapter
import ru.futurio.game.model.manipulation.impl.MoveCommandStartableAdapter
import java.util.concurrent.LinkedBlockingDeque
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StartMoveTest {

    private val objMap: MutableMap<String, UObject?> = mutableMapOf()
    private val cmdCtx: CommandContext = mock { ctx ->
        on(ctx.getUObjectById(any())).thenAnswer { objMap[it.arguments[0]] }
    }

    @BeforeEach
    fun reset() {
        objMap.clear()
    }

    @Test
    fun `start move`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val startMoveOrder = UObject(
            MANIPULATED_OBJECT_ID to spaceShip.id,
            MOVING_DIRECTION to Direction(30.0, 0.0),
            VELOCITY_MODULUS to 10,
            COMMAND_QUEUE to commandQueue
        )

        StartMoveCommand(MoveCommandStartableAdapter(startMoveOrder)).execute(cmdCtx)

        MovingAdapter(spaceShip).let {
            assertEquals(Positioning(1.0, 2.0, 0.0), it.position)
            assertEquals(Positioning(8.660254, 5.0, 0.0), it.velocity)
        }
        assertEquals(1, commandQueue.size)
    }

    @Test
    fun `start move fails due to missing object id`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val startMoveOrder = UObject(
            MOVING_DIRECTION to Direction(30.0, 0.0),
            VELOCITY_MODULUS to 10,
            COMMAND_QUEUE to commandQueue
        )

        assertThrows<IllegalStateException> {
            StartMoveCommand(MoveCommandStartableAdapter(startMoveOrder)).execute(cmdCtx)
        }.let {
            assertEquals("Manipulated object ID is not defined for start move command", it.message)
        }
        assertNull(MovingAdapter(spaceShip).velocity)
        assertEquals(0, commandQueue.size)
    }

    @Test
    fun `start move fails due to not found managed object`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val startMoveOrder = UObject(
            MANIPULATED_OBJECT_ID to "wrong id",
            MOVING_DIRECTION to Direction(30.0, 0.0),
            VELOCITY_MODULUS to 10,
            COMMAND_QUEUE to commandQueue
        )

        assertThrows<IllegalArgumentException> {
            StartMoveCommand(MoveCommandStartableAdapter(startMoveOrder)).execute(cmdCtx)
        }.let {
            assertEquals("Manipulated object not found by ID", it.message)
        }
        assertEquals(0, commandQueue.size)
    }

    @Test
    fun `start move with missing direction`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val startMoveOrder = UObject(
            MANIPULATED_OBJECT_ID to spaceShip.id,
            VELOCITY_MODULUS to 10,
            COMMAND_QUEUE to commandQueue
        )

        assertThrows<IllegalStateException> {
            StartMoveCommand(MoveCommandStartableAdapter(startMoveOrder)).execute(cmdCtx)
        }.let {
            assertEquals("Start move direction is not defined", it.message)
        }
        assertNull(MovingAdapter(spaceShip).velocity)
        assertEquals(0, commandQueue.size)
    }

    @Test
    fun `start move with missing vector modulus`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val startMoveOrder = UObject(
            MANIPULATED_OBJECT_ID to spaceShip.id,
            MOVING_DIRECTION to Direction(30.0, 0.0),
            COMMAND_QUEUE to commandQueue
        )

        assertThrows<IllegalStateException> {
            StartMoveCommand(MoveCommandStartableAdapter(startMoveOrder)).execute(cmdCtx)
        }.let {
            assertEquals("Velocity modulus is not defined", it.message)
        }
        assertNull(MovingAdapter(spaceShip).velocity)
        assertEquals(0, commandQueue.size)
    }

    @Test
    fun `start move with missing queue`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val startMoveOrder = UObject(
            MANIPULATED_OBJECT_ID to spaceShip.id,
            MOVING_DIRECTION to Direction(30.0, 0.0),
            VELOCITY_MODULUS to 10
        )

        assertThrows<IllegalStateException> {
            StartMoveCommand(MoveCommandStartableAdapter(startMoveOrder)).execute(cmdCtx)
        }.let {
            assertEquals("Command queue is not defined for start move command", it.message)
        }
        assertEquals(0, commandQueue.size)
    }
}
