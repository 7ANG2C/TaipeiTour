package com.fang.taipeitour.model.language

import java.util.Locale

/**
 * @property key data store storage, DO NOT modify
 */
enum class Language(val locale: Locale, val key: String) {
    TAIWAN(Locale.TAIWAN, "tw"),
    CHINA(Locale.CHINA, "cn"),
    ENGLISH(Locale.US, "en"),
    JAPAN(Locale.JAPAN, "ja"),
    KOREA(Locale.KOREA, "ko"),
    SPAN(Locale("es", "ES"), "es"),
    INDONESIA(Locale("in", "ID"), "id"),
    THAILAND(Locale("th", "TH"), "th"),
    VIETNAM(Locale("vi", "VN"), "vi"),
    ;

    companion object {
        val default = ENGLISH
        val all by lazy { values().toList() }
        operator fun get(key: String) = all.find { it.key == key }
    }
}
