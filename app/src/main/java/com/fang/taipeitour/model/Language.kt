package com.fang.taipeitour.model

import android.content.Context
import android.content.res.Configuration
import com.fang.taipeitour.R
import java.util.Locale

enum class Language(val display: String) {
    TAIWAN("zh-tw"),
    CHINA("zh-cn"),
    ENGLISH("en"),
    JAPAN("ja"),
    KOREA("ko"),
    SPANISH("es"),
    INDIEN("id"),
    THAI("th"),
    VIENEM("vi"),
    ;
}

fun Language.get(context: Context, languageCode: String, countryCode: String): String {
    val locale = Locale(languageCode, countryCode)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    val localizedResources = context.createConfigurationContext(config).resources
    return localizedResources.getString(R.string.language)
}