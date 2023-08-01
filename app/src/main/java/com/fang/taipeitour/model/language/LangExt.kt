package com.fang.taipeitour.model.language

import android.content.Context
import android.content.res.Configuration
import com.fang.taipeitour.R

/**
 * 1. 拿不到就拿預設
 * 在拿不到 null
 * exception return null
 */
fun Language.getLocaleString(context: Context, id: Int): String? {
    return kotlin.runCatching {
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        val localizedResources = context.createConfigurationContext(config).resources
        localizedResources.getString(id)
    }.getOrNull()
}

val Language.res
    get() = when (this) {
        Language.TAIWAN -> R.drawable.flag_tw
        Language.CHINA -> R.drawable.flag_cn
        Language.ENGLISH -> R.drawable.flag_us
        Language.JAPAN -> R.drawable.flag_jp
        Language.KOREA -> R.drawable.flag_kr
        Language.SPAN -> R.drawable.flag_es
        Language.INDONESIA -> R.drawable.flag_id
        Language.THAILAND -> R.drawable.flag_th
        Language.VIETNAM -> R.drawable.flag_vn
    }
