package ru.futurio.model.manipulation

import ru.futurio.command.Command
import java.util.*

interface MoveCommandEndable {
    val manipulatedObjectId: String?
    val commandQueue: Queue<Command<*>>?
}
