package ru.futurio.game.model.ability

import ru.futurio.game.model.ManipulatedObject
import ru.futurio.game.model.Positioning

interface Moving : ManipulatedObject {
    var position: Positioning?
    val velocity: Positioning?
}
