package ru.futurio.model.ability.impl

import ru.futurio.model.ability.Movable
import ru.futurio.model.Positioning
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty.VELOCITY_VECTOR

class MovableAdapter(private val uObject: UObject) : Movable {
    override val id: String
        get() = uObject.id

    override var velocity: Positioning?
        get() = uObject.getProperty(VELOCITY_VECTOR)
        set(value) {
            uObject.setProperty(VELOCITY_VECTOR, value)
        }
}
