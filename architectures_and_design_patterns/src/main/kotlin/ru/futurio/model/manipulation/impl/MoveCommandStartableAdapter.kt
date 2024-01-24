package ru.futurio.model.manipulation.impl

import ru.futurio.command.Command
import ru.futurio.model.Direction
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty.*
import ru.futurio.model.manipulation.MoveCommandStartable
import java.util.*

class MoveCommandStartableAdapter(private val uObject: UObject) : MoveCommandStartable {
    override val manipulatedObjectId: String?
        get() = uObject.getProperty(MANIPULATED_OBJECT_ID)
    override val velocityModulus: Int?
        get() = uObject.getProperty(VELOCITY_MODULUS)
    override val movingDirection: Direction?
        get() = uObject.getProperty(MOVING_DIRECTION)
    override val commandQueue: Queue<Command<*>>?
        get() = uObject.getProperty(COMMAND_QUEUE)
}
