package org.example.ru.futurio.model.impl

import org.example.ru.futurio.model.Moveable
import org.example.ru.futurio.model.Positioning
import org.example.ru.futurio.model.Turnable

data class SpaceShip(
    override var position: Positioning?,
    override var velocity: Positioning?
) : Moveable, Turnable
