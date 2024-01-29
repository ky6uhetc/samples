package ru.futurio.game.model.manipulation

import ru.futurio.game.command.Command
import ru.futurio.game.model.Direction
import java.util.*

interface MoveCommandStartable {
    val manipulatedObjectId: String?
    val velocityModulus: Int?
    val movingDirection: Direction?
    val commandQueue: Queue<Command<*>>?
}
