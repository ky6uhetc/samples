package ru.futurio.model.ability.impl

import ru.futurio.model.ability.Moving
import ru.futurio.model.Positioning
import ru.futurio.model.UObject
import ru.futurio.model.UObjectProperty.POSITION
import ru.futurio.model.UObjectProperty.VELOCITY_VECTOR

class MovingAdapter(private val uObject: UObject) : Moving {
    override val id: String
        get() = uObject.id
    override var position: Positioning?
        get() = uObject.getProperty(POSITION)
        set(value) {
            uObject.setProperty(POSITION, value)
        }
    override val velocity: Positioning?
        get() = uObject.getProperty(VELOCITY_VECTOR)

}
