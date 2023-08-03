package com.fang.taipeitour.model.language

import java.util.Locale

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
        val default = TAIWAN
        val all by lazy { values().toList() }
        fun findByKeyOrDefault(key: String, default: Language = this.default): Language {
            return all.find { it.key == key } ?: default
        }
        operator fun get(index: Int) = all[index]
    }
}
