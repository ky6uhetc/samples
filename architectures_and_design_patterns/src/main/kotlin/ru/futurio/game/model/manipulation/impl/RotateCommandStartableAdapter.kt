package ru.futurio.game.model.manipulation.impl

import ru.futurio.game.command.Command
import ru.futurio.game.model.Rotation
import ru.futurio.game.model.UObject
import ru.futurio.game.model.UObjectProperty.*
import ru.futurio.game.model.manipulation.RotateCommandStartable
import java.util.*

class RotateCommandStartableAdapter(private val uObject: UObject) : RotateCommandStartable {
    override val manipulatedObjectId: String?
        get() = uObject.getProperty(MANIPULATED_OBJECT_ID)
    override val rotation: Rotation?
        get() = uObject.getProperty(ROTATION)
    override val commandQueue: Queue<Command<*>>?
        get() = uObject.getProperty(COMMAND_QUEUE)
}
