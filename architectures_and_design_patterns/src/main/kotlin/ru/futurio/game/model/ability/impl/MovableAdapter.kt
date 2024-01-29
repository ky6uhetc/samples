package ru.futurio.game.model.ability.impl

import ru.futurio.game.model.ability.Movable
import ru.futurio.game.model.Positioning
import ru.futurio.game.model.UObject
import ru.futurio.game.model.UObjectProperty.VELOCITY_VECTOR

class MovableAdapter(private val uObject: UObject) : Movable {
    override val id: String
        get() = uObject.id

    override var velocity: Positioning?
        get() = uObject.getProperty(VELOCITY_VECTOR)
        set(value) {
            uObject.setProperty(VELOCITY_VECTOR, value)
        }
}
