package org.example.ru.futurio.model.impl

import org.example.ru.futurio.model.Moveable
import org.example.ru.futurio.model.Positioning

data class SpaceShip(
    override var position: Positioning?,
    override val velocity: Positioning?
) : Moveable
