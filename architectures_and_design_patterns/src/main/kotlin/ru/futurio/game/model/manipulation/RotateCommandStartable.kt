package ru.futurio.game.model.manipulation

import ru.futurio.game.command.Command
import ru.futurio.game.model.Rotation
import java.util.*

interface RotateCommandStartable {
    val manipulatedObjectId: String?
    val rotation: Rotation?
    val commandQueue: Queue<Command<*>>?
}
