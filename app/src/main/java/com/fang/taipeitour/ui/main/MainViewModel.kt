package com.fang.taipeitour.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.extension.stateIn
import com.fang.taipeitour.model.DarkMode
import com.fang.taipeitour.model.language.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(
    repository: UserPreferencesRepository
) : ViewModel() {

    val darkModeState = repository.getDarkMode()
        .mapLatest {
            it ?: DarkMode.default
        }
        .catch { emit(DarkMode.default) }
        .stateIn(
            scope = viewModelScope,
            initialValue = null
        )

    val languageState = repository.getLanguage()
        .catch { emit(Language.default) }
        .stateIn(
            scope = viewModelScope,
            initialValue = null
        )
}
