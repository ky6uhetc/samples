package ru.futurio.model.impl

import ru.futurio.model.Movable
import ru.futurio.model.Positioning
import ru.futurio.model.Turnable

data class SpaceShip(
    override var position: Positioning?,
    override var velocity: Positioning?
) : Movable, Turnable
