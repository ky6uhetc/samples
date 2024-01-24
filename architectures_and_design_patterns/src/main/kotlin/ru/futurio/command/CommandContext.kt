package ru.futurio.command

import ru.futurio.model.UObject

interface CommandContext {
    fun getUObjectById(id: String): UObject
}
