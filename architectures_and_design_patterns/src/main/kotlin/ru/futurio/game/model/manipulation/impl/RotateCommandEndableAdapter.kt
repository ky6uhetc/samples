package ru.futurio.game.model.manipulation.impl

import ru.futurio.game.command.Command
import ru.futurio.game.model.UObject
import ru.futurio.game.model.UObjectProperty.COMMAND_QUEUE
import ru.futurio.game.model.UObjectProperty.MANIPULATED_OBJECT_ID
import ru.futurio.game.model.manipulation.RotateCommandEndable
import java.util.*

class RotateCommandEndableAdapter(private val uObject: UObject) : RotateCommandEndable {
    override val manipulatedObjectId: String?
        get() = uObject.getProperty(MANIPULATED_OBJECT_ID)
    override val commandQueue: Queue<Command<*>>?
        get() = uObject.getProperty(COMMAND_QUEUE)
}
