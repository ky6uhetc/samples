package ru.futurio.game.model.ability.impl

import ru.futurio.game.model.ability.Moving
import ru.futurio.game.model.Positioning
import ru.futurio.game.model.UObject
import ru.futurio.game.model.UObjectProperty.POSITION
import ru.futurio.game.model.UObjectProperty.VELOCITY_VECTOR

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
