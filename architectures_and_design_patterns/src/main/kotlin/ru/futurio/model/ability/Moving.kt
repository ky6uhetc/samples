package ru.futurio.model.ability

import ru.futurio.model.ManipulatedObject
import ru.futurio.model.Positioning

interface Moving : ManipulatedObject {
    var position: Positioning?
    val velocity: Positioning?
}
