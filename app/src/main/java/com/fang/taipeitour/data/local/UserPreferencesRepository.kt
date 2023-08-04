package com.fang.taipeitour.data.local

import com.fang.taipeitour.data.local.user.UserPreferencesDataStore
import com.fang.taipeitour.model.DarkMode
import com.fang.taipeitour.model.language.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

/**
 * user app preferences repository
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferencesRepository(
    private val dataStore: UserPreferencesDataStore
) {

    fun getLanguage() = dataStore.invoke()
        .mapLatest {
            Language.findByKeyOrDefault(it.language)
        }
        .distinctUntilChanged()

    fun getDarkMode() = dataStore.invoke()
        .mapLatest {
            DarkMode.findByKey(it.darkMode)
        }
        .distinctUntilChanged()

    suspend fun seLanguage(language: Language) {
        dataStore.setLanguage(language.key)
    }

    suspend fun toggleDarkMode() {
        dataStore.toggleDarkMode()
    }

    suspend fun reset() {
        dataStore.reset()
    }
}
