package ru.futurio.model

import java.util.UUID

data class UObject(
    val id: String = UUID.randomUUID().toString()
) {
    private val properties = HashMap<String, Any?>()

    constructor(vararg values: Pair<UObjectProperty, Any?>) : this() {
        values.forEach { (key, value) -> properties[key.name] = value }
    }

    fun <T> getProperty(key: UObjectProperty): T? = properties[key.name] as? T

    fun setProperty(key: UObjectProperty, value: Any?) = properties.put(key.name, value)
}
