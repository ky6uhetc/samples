package ru.futurio.game.model.manipulation

import ru.futurio.game.command.Command
import java.util.*

interface MoveCommandEndable {
    val manipulatedObjectId: String?
    val commandQueue: Queue<Command<*>>?
}
