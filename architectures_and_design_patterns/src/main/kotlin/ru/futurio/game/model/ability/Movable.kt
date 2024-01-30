package ru.futurio.game.model.ability

import ru.futurio.game.model.ManipulatedObject
import ru.futurio.game.model.Positioning

interface Movable : ManipulatedObject {
    var velocity: Positioning?
}
