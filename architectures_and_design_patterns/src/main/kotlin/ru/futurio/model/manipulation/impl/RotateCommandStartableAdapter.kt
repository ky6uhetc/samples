package ru.futurio.model.manipulation.impl

import ru.futurio.command.Command
import ru.futurio.model.Rotation
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty.*
import ru.futurio.model.manipulation.RotateCommandStartable
import java.util.*

class RotateCommandStartableAdapter(private val uObject: UObject) : RotateCommandStartable {
    override val manipulatedObjectId: String?
        get() = uObject.getProperty(MANIPULATED_OBJECT_ID)
    override val rotation: Rotation?
        get() = uObject.getProperty(ROTATION)
    override val commandQueue: Queue<Command<*>>?
        get() = uObject.getProperty(COMMAND_QUEUE)
}
