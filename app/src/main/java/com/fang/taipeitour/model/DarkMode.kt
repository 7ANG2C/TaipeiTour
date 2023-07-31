package com.fang.taipeitour.model

/**
 * !! DO NOT !!
 */
enum class DarkMode(val key: String) {
    ENABLED("enabled"), DISABLED("disabled"),
    ;

    companion object {
        val default = ENABLED
        val all by lazy { values().toList() }
        fun findByKey(key: String) = all.find { it.key == key }
    }

    val enabled get() = this == ENABLED
}
