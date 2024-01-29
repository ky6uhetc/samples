package ru.futurio.game

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import ru.futurio.game.command.CommandContext
import ru.futurio.game.command.RotateCommand
import ru.futurio.game.command.StopRotateCommand
import ru.futurio.game.model.Axis.Z
import ru.futurio.game.model.Direction
import ru.futurio.game.model.Positioning
import ru.futurio.game.model.Rotation
import ru.futurio.game.model.UObject
import ru.futurio.game.model.UObjectProperty.*
import ru.futurio.game.model.ability.impl.MovableAdapter
import ru.futurio.game.model.manipulation.impl.RotateCommandEndableAdapter
import java.util.concurrent.LinkedBlockingDeque
import kotlin.test.assertEquals

class StopRotateTest {

    private val objMap: MutableMap<String, UObject?> = mutableMapOf()
    private val cmdCtx: CommandContext = mock { ctx ->
        on(ctx.getUObjectById(any())).thenAnswer { objMap[it.arguments[0]] }
    }

    @BeforeEach
    fun reset() {
        objMap.clear()
    }

    @Test
    fun `stop rotate`() {
        val spaceShip = UObject(
            POSITION to Positioning(1.0, 2.0, 0.0),
            VELOCITY_VECTOR to Positioning(0.0, 5.0, 0.0)
        ).also { objMap[it.id] = it }

        val commandQueue = LinkedBlockingDeque<Any>().also {
            it.add(RotateCommand(MovableAdapter(spaceShip), Rotation(30.0, Z)))
        }

        val stopRotateOrder = RotateCommandEndableAdapter(
            UObject(
                MANIPULATED_OBJECT_ID to spaceShip.id,
                COMMAND_QUEUE to commandQueue
            )
        )

        StopRotateCommand(stopRotateOrder).execute(cmdCtx)

        assertEquals(0, commandQueue.size)
    }

    @Test
    fun `stop rotate fails due to missing object id`() {
        val spaceShip = UObject(
            POSITION to Positioning(1.0, 2.0, 0.0),
            VELOCITY_VECTOR to Positioning(0.0, 5.0, 0.0)
        ).also { objMap[it.id] = it }

        val commandQueue = LinkedBlockingDeque<Any>().also {
            it.add(RotateCommand(MovableAdapter(spaceShip), Rotation(30.0, Z)))
        }

        val stopRotateOrder = RotateCommandEndableAdapter(
            UObject(
                MOVING_DIRECTION to Direction(30.0, 0.0),
                COMMAND_QUEUE to commandQueue
            )
        )

        assertThrows<IllegalStateException> {
            StopRotateCommand(stopRotateOrder).execute(cmdCtx)
        }.let {
            assertEquals("Manipulated object ID is not defined for stop rotate command", it.message)
        }
        assertEquals(1, commandQueue.size)
    }

    @Test
    fun `stop rotate fails due to not found managed object`() {
        val spaceShip = UObject(
            POSITION to Positioning(1.0, 2.0, 0.0),
            VELOCITY_VECTOR to Positioning(0.0, 5.0, 0.0)
        ).also { objMap[it.id] = it }

        val commandQueue = LinkedBlockingDeque<Any>().also {
            it.add(RotateCommand(MovableAdapter(spaceShip), Rotation(30.0, Z)))
        }

        val stopRotateOrder = RotateCommandEndableAdapter(
            UObject(
                MANIPULATED_OBJECT_ID to "wrong id",
                MOVING_DIRECTION to Direction(30.0, 0.0),
                VELOCITY_MODULUS to 10,
                COMMAND_QUEUE to commandQueue
            )
        )

        assertThrows<IllegalArgumentException> {
            StopRotateCommand(stopRotateOrder).execute(cmdCtx)
        }.let {
            assertEquals("Manipulated object not found by ID", it.message)
        }
        assertEquals(1, commandQueue.size)
    }

    @Test
    fun `stop rotate with missing queue`() {
        val spaceShip = UObject(
            POSITION to Positioning(1.0, 2.0, 0.0),
            VELOCITY_VECTOR to Positioning(0.0, 5.0, 0.0)
        ).also { objMap[it.id] = it }

        val commandQueue = LinkedBlockingDeque<Any>().also {
            it.add(RotateCommand(MovableAdapter(spaceShip), Rotation(30.0, Z)))
        }

        val stopRotateOrder = RotateCommandEndableAdapter(
            UObject(
                MANIPULATED_OBJECT_ID to spaceShip.id,
                VELOCITY_MODULUS to 10
            )
        )

        assertThrows<IllegalStateException> {
            StopRotateCommand(stopRotateOrder).execute(cmdCtx)
        }.let {
            assertEquals("Command queue is not defined for stop rotate command", it.message)
        }
        assertEquals(1, commandQueue.size)
    }

    @Test
    fun `stop rotate with missing RotateCommand in queue`() {
        val spaceShip1 = UObject(
            POSITION to Positioning(1.0, 2.0, 0.0),
            VELOCITY_VECTOR to Positioning(0.0, 5.0, 0.0)
        ).also { objMap[it.id] = it }

        val spaceShip2 = UObject(
            POSITION to Positioning(1.0, 2.0, 0.0),
            VELOCITY_VECTOR to Positioning(2.0, 0.0, 0.0)
        ).also { objMap[it.id] = it }

        val commandQueue = LinkedBlockingDeque<Any>().also {
            it.add(RotateCommand(MovableAdapter(spaceShip1), Rotation(30.0, Z)))
            it.add(RotateCommand(MovableAdapter(spaceShip2), Rotation(90.0, Z)))
        }

        val stopRotateOrder = RotateCommandEndableAdapter(
            UObject(
                MANIPULATED_OBJECT_ID to spaceShip1.id,
                COMMAND_QUEUE to commandQueue
            )
        )

        assertDoesNotThrow {
            StopRotateCommand(stopRotateOrder).execute(cmdCtx)
        }
        assertEquals(1, commandQueue.size)
    }
}
