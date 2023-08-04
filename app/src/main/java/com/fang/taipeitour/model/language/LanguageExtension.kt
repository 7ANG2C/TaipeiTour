package com.fang.taipeitour.model.language

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.fang.taipeitour.R

/**
 * 1. 拿不到就拿預設
 * 在拿不到 null
 * exception return null
 */
@Composable
fun Language?.getLocaleString(@StringRes res: Int, default: String = ""): String {
    return if (this != null) {
        kotlin.runCatching {
            val config = LocalConfiguration.current
            config.setLocale(locale)
            val localizedResources =
                LocalContext.current.createConfigurationContext(config).resources
            localizedResources.getString(res)
        }.getOrDefault(default)
    } else ""
}

val Language.flag
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
