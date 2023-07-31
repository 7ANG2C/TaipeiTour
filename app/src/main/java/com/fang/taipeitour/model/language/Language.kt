package com.fang.taipeitour.model.language

import java.util.Locale

enum class Language(val locale: Locale, val display: String, val key: String) {
    TAIWAN(Locale.TAIWAN, "zh-tw", "tw"),
    CHINA(Locale.CHINA, "zh-cn", "cn"),
    ENGLISH(Locale.US, "en", "en"),
    JAPAN(Locale.JAPAN, "ja", "ja"),
    KOREA(Locale.KOREA, "ko", "ko"),
    SPAN(Locale("es", "ES"), "es", "es"),
    INDONESIA(Locale("in", "ID"), "id", "id"),
    THAILAND(Locale("th", "TH"), "th", "th"),
    VIETNAM(Locale("vi", "VN"), "vi", "vi"),
    ;

    companion object {
        val all by lazy { values().toList() }
    }
}
