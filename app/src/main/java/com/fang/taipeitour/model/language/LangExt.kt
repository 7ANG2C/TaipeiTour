package com.fang.taipeitour.model.language

import android.content.Context
import android.content.res.Configuration
import com.fang.taipeitour.R

/**
 * 1. 拿不到就拿預設
 * 在拿不到 null
 * exception return null
 */
fun Language.getLocaleString(context: Context, id: Int = R.string.language): String? {
    return kotlin.runCatching {
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        val localizedResources = context.createConfigurationContext(config).resources
        localizedResources.getString(id)
    }.getOrNull()
}

val Language.res
    get() = when (this) {
        Language.TAIWAN -> R.drawable.tw
        Language.CHINA -> R.drawable.cn
        Language.ENGLISH -> R.drawable.us
        Language.JAPAN -> R.drawable.jp
        Language.KOREA -> R.drawable.kr
        Language.SPAN -> R.drawable.es
        Language.INDONESIA -> R.drawable.id
        Language.THAILAND -> R.drawable.th
        Language.VIETNAM -> R.drawable.vn
    }
