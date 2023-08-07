package com.fang.taipeitour.model

/**
 * @property key data store storage, DO NOT modify
 */
enum class DarkMode(val key: String) {
    ENABLED("enabled"), DISABLED("disabled"),
    ;

    companion object {
        val default = ENABLED
        operator fun get(key: String) = values().toList().find { it.key == key }
    }

    val enabled get() = this == ENABLED
}
