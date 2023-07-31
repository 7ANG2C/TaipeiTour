package com.fang.taipeitour.model

/**
 * !! DO NOT !!
 */
enum class DarkMode(val key: String) {
    ENABLED("enabled"), DISABLED("disabled"),
    ;

    companion object {
        val default by lazy { DarkMode.ENABLED }
        val all by lazy { values().toList() }
        fun findByKeyOrDefault(key: String, default: DarkMode = this.default) {
            all.find { it.key == key } ?: default
        }
    }
}
