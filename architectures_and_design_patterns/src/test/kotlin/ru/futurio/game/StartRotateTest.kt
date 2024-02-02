package ru.futurio.game

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import ru.futurio.game.command.*
import ru.futurio.game.model.*
import ru.futurio.game.model.UObjectProperty.*
import ru.futurio.game.model.ability.Movable
import ru.futurio.game.model.ability.impl.MovingAdapter
import ru.futurio.game.model.manipulation.impl.RotateCommandStartableAdapter
import java.util.concurrent.LinkedBlockingDeque
import kotlin.test.assertEquals

class StartRotateTest {

    private val objMap: MutableMap<String, UObject?> = mutableMapOf()
    private val cmdCtx: CommandContext = mock { ctx ->
        on(ctx.getUObjectById(any())).thenAnswer { objMap[it.arguments[0]] }
    }

    @BeforeEach
    fun reset() {
        objMap.clear()
    }

    @Test
    fun `start turn`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val spaceShip = UObject(
            POSITION to Positioning(1.0, 2.0, 0.0),
            VELOCITY_VECTOR to Positioning(2.0, 0.0, 0.0)
        ).also { objMap[it.id] = it }
        val startRotateOrder = UObject(
            MANIPULATED_OBJECT_ID to spaceShip.id,
            ROTATION to Rotation(30.0, Axis.Z),
            COMMAND_QUEUE to commandQueue
        )

        StartRotateCommand(RotateCommandStartableAdapter(startRotateOrder)).execute(cmdCtx)

        val spaceShipMoving = MovingAdapter(spaceShip)
        assertEquals(Positioning(1.0, 2.0, 0.0), spaceShipMoving.position)
        assertEquals(Positioning(2.0, 0.0, 0.0), spaceShipMoving.velocity)

        assertEquals(1, commandQueue.size)
        (commandQueue.pop() as BridgedCommand<Movable>).let {
            assertEquals(spaceShip.id, it.subject.id)
            it.execute(cmdCtx)
        }
        assertEquals(Positioning(1.7320508, 1.0, 0.0), spaceShipMoving.velocity)
    }

    @Test
    fun `start turn fails due to missing object id`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val startRotateOrder = UObject(
            ROTATION to Rotation(30.0, Axis.Z),
            COMMAND_QUEUE to commandQueue
        )

        assertThrows<IllegalStateException> {
            StartRotateCommand(RotateCommandStartableAdapter(startRotateOrder)).execute(cmdCtx)
        }.let {
            assertEquals("Manipulated object ID is not defined for start rotate command", it.message)
        }
        assertEquals(0, commandQueue.size)
    }

    @Test
    fun `start turn fails due to not found managed object`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val startRotateOrder = UObject(
            MANIPULATED_OBJECT_ID to "wrong id",
            MOVING_DIRECTION to Direction(30.0, 0.0),
            VELOCITY_MODULUS to 10,
            COMMAND_QUEUE to commandQueue
        )

        assertThrows<IllegalArgumentException> {
            StartRotateCommand(RotateCommandStartableAdapter(startRotateOrder)).execute(cmdCtx)
        }.let {
            assertEquals("Manipulated object not found by ID", it.message)
        }
        assertEquals(0, commandQueue.size)
    }

    @Test
    fun `start turn with missing rotation`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val startRotateOrder = UObject(
            MANIPULATED_OBJECT_ID to spaceShip.id,
            COMMAND_QUEUE to commandQueue
        )

        assertThrows<IllegalStateException> {
            StartRotateCommand(RotateCommandStartableAdapter(startRotateOrder)).execute(cmdCtx)
        }.let {
            assertEquals("Start rotate direction is not defined", it.message)
        }
        assertEquals(0, commandQueue.size)
    }

    @Test
    fun `start turn with missing queue`() {
        val commandQueue = LinkedBlockingDeque<Command<Any>>()
        val spaceShip = UObject(POSITION to Positioning(1.0, 2.0, 0.0)).also { objMap[it.id] = it }
        val startRotateOrder = UObject(
            MANIPULATED_OBJECT_ID to spaceShip.id,
            ROTATION to Rotation(30.0, Axis.Z),
            VELOCITY_MODULUS to 10
        )

        assertThrows<IllegalStateException> {
            StartRotateCommand(RotateCommandStartableAdapter(startRotateOrder)).execute(cmdCtx)
        }.let {
            assertEquals("Command queue is not defined for start rotate command", it.message)
        }
        assertEquals(0, commandQueue.size)
    }
}
