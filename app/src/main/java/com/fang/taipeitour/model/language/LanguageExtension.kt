package com.fang.taipeitour.model.language

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Composable
fun Language?.getLocaleString(
    @StringRes res: Int,
    default: String = "",
): String {
    return if (this != null) {
        kotlin.runCatching {
            val config = LocalConfiguration.current
            config.setLocale(locale)
            val localizedResources =
                LocalContext.current.createConfigurationContext(config).resources
            localizedResources.getString(res)
        }.getOrDefault(default)
    } else {
        ""
    }
}
