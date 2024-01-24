package ru.futurio.model.manipulation.impl

import ru.futurio.command.Command
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty.*
import ru.futurio.model.manipulation.MoveCommandEndable
import java.util.*

class MoveCommandEndableAdapter(private val uObject: UObject) : MoveCommandEndable {

    override val manipulatedObjectId: String?
        get() = uObject.getProperty(MANIPULATED_OBJECT_ID)
    override val commandQueue: Queue<Command<*>>?
        get() = uObject.getProperty(COMMAND_QUEUE)
}
