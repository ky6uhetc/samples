package ru.futurio

import org.example.ru.futurio.model.Positioning
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class PositioningTest {

    @Test
    fun `add position shift`() {
        val position = Positioning(1.0, 7.0)

        assertEquals(Positioning(3.0, 10.0), position.add(Positioning(2.0, 3.0)))
        assertEquals(Positioning(-2.0, 7.0), position.add(Positioning(-3.0, 0.0)))
        assertEquals(Positioning(2.0, 6.0, 5.0), position.add(Positioning(1.0, -1.0, 5.0)))
    }
}
