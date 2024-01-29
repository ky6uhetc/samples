package ru.futurio.game.model.manipulation

import ru.futurio.game.command.Command
import java.util.*

interface RotateCommandEndable {
    val manipulatedObjectId: String?
    val commandQueue: Queue<Command<*>>?
}
