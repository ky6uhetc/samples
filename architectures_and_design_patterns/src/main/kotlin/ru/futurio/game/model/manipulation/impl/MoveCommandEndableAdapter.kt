package ru.futurio.game.model.manipulation.impl

import ru.futurio.game.command.Command
import ru.futurio.game.model.UObject
import ru.futurio.game.model.UObjectProperty.*
import ru.futurio.game.model.manipulation.MoveCommandEndable
import java.util.*

class MoveCommandEndableAdapter(private val uObject: UObject) : MoveCommandEndable {

    override val manipulatedObjectId: String?
        get() = uObject.getProperty(MANIPULATED_OBJECT_ID)
    override val commandQueue: Queue<Command<*>>?
        get() = uObject.getProperty(COMMAND_QUEUE)
}
