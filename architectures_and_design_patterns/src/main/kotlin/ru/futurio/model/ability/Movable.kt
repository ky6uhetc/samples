package ru.futurio.model.ability

import ru.futurio.model.ManipulatedObject
import ru.futurio.model.Positioning

interface Movable : ManipulatedObject {
    var velocity: Positioning?
}
