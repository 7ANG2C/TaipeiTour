package com.fang.taipeitour.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.extension.stateIn
import com.fang.taipeitour.model.DarkMode
import com.fang.taipeitour.model.language.Language
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

class SettingViewModel(
    private val repository: UserPreferencesRepository,
    private val settingFlavorBehavior: SettingFlavorBehavior
) : ViewModel() {

    val darkModeState = repository.getDarkMode()
        .mapLatest {
            it ?: DarkMode.default
        }
        .catch { emit(DarkMode.default) }
        .stateIn(
            scope = viewModelScope,
            initialValue = DarkMode.default
        )

    val languageState = repository.getLanguage()
        .catch { emit(Language.default) }
        .stateIn(
            scope = viewModelScope,
            initialValue = Language.default
        )

    fun setLanguage(l: Language) {
        viewModelScope.launch {
            repository.seLanguage(l)
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            repository.toggleDarkMode()
        }
    }

    fun reset() {
        viewModelScope.launch {
            repository.reset()
        }
    }
}
