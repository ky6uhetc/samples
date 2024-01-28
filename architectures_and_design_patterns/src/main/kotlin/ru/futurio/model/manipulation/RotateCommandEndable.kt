package ru.futurio.model.manipulation

import ru.futurio.command.Command
import java.util.*

interface RotateCommandEndable {
    val manipulatedObjectId: String?
    val commandQueue: Queue<Command<*>>?
}
