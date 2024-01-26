package ru.futurio.model.manipulation

import ru.futurio.command.Command
import ru.futurio.model.Rotation
import java.util.*

interface RotateCommandStartable {
    val manipulatedObjectId: String?
    val rotation: Rotation?
    val commandQueue: Queue<Command<*>>?
}
