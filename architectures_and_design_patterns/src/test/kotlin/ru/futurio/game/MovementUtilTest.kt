package ru.futurio.game

import org.junit.jupiter.api.Test
import ru.futurio.game.util.MovementUtil
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import kotlin.math.pow
import kotlin.test.assertEquals

class MovementUtilTest {

    @Test
    fun `cos for degrees`() {
        assertEquals(1.0, MovementUtil.cos(0.0))
        assertEquals(BigDecimal.valueOf(3.0.pow(0.5) / 2).setScale(8, HALF_UP).toDouble(), MovementUtil.cos(30.0))
        assertEquals(BigDecimal.valueOf(1 / 2.0.pow(0.5)).setScale(8, HALF_UP).toDouble(), MovementUtil.cos(45.0))
        assertEquals(0.5, MovementUtil.cos(60.0))
        assertEquals(0.0, MovementUtil.cos(90.0))
    }

    @Test
    fun `sin for degrees`() {
        assertEquals(1.0, MovementUtil.sin(90.0))
        assertEquals(BigDecimal.valueOf(3.0.pow(0.5) / 2).setScale(8, HALF_UP).toDouble(), MovementUtil.sin(60.0))
        assertEquals(BigDecimal.valueOf(1 / 2.0.pow(0.5)).setScale(8, HALF_UP).toDouble(), MovementUtil.sin(45.0))
        assertEquals(0.5, MovementUtil.sin(30.0))
        assertEquals(0.0, MovementUtil.sin(0.0))
    }
}
