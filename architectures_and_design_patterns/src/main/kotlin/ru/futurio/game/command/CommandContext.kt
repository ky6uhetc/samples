package ru.futurio.game.command

import ru.futurio.game.model.UObject

interface CommandContext {
    fun getUObjectById(id: String): UObject
}
