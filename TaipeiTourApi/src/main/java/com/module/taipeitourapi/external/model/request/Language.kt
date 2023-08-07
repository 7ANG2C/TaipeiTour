package com.module.taipeitourapi.external.model.request

/**
 * Request Language
 */
enum class Language(internal val requestCode: String) {
    TAIWAN("zh-tw"),
    CHINA("zh-cn"),
    ENGLISH("en"),
    JAPAN("ja"),
    KOREA("ko"),
    SPAN("es"),
    INDONESIA("id"),
    THAILAND("th"),
    VIETNAM("vi"),
    ;
}
