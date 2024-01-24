package ru.futurio.model.manipulation

import ru.futurio.command.Command
import ru.futurio.model.Direction
import java.util.*

interface MoveCommandStartable {
    val manipulatedObjectId: String?
    val velocityModulus: Int?
    val movingDirection: Direction?
    val commandQueue: Queue<Command<*>>?
}
