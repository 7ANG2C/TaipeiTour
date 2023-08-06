package com.fang.taipeitour.data.local

import com.fang.taipeitour.model.ColorScheme
import com.fang.taipeitour.model.DarkMode
import com.fang.taipeitour.model.language.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

/**
 * user app preferences repository
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferencesRepository(
    private val dataStore: UserPreferencesDataStore
) {

    operator fun invoke() = dataStore.invoke()
        .mapLatest {
            val default = UserPreferences.default
            UserPreferences(
                language = Language[it.language] ?: default.language,
                darkMode = DarkMode[it.darkMode] ?: default.darkMode,
                colorScheme = ColorScheme[it.colorScheme] ?: default.colorScheme,
            )
        }
        .flowOn(Dispatchers.Default)

    fun getLanguage() = invoke()
        .mapLatest { it.language }
        .distinctUntilChanged()

    fun isDefault() = dataStore.invoke()
        .mapLatest {
            it.language.isEmpty() &&
                it.darkMode.isEmpty() &&
                it.colorScheme.isEmpty()
        }

    suspend fun seLanguage(language: Language) {
        dataStore.setLanguage(language.key)
    }

    suspend fun toggleDarkMode() {
        dataStore.toggleDarkMode()
    }

    suspend fun toggleColorScheme() {
        dataStore.toggleColorScheme()
    }

    suspend fun resetUserPreferences() {
        dataStore.reset()
    }
}
