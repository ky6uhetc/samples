package ru.futurio.model.manipulation.impl

import ru.futurio.command.Command
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty.COMMAND_QUEUE
import ru.futurio.model.UObjectProperty.MANIPULATED_OBJECT_ID
import ru.futurio.model.manipulation.RotateCommandEndable
import java.util.*

class RotateCommandEndableAdapter(private val uObject: UObject) : RotateCommandEndable {
    override val manipulatedObjectId: String?
        get() = uObject.getProperty(MANIPULATED_OBJECT_ID)
    override val commandQueue: Queue<Command<*>>?
        get() = uObject.getProperty(COMMAND_QUEUE)
}
