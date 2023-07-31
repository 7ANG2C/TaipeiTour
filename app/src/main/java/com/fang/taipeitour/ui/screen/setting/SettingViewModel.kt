package com.fang.taipeitour.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.model.DarkMode
import com.fang.taipeitour.model.language.Language
import com.fang.taipeitour.ui.component.sharingStartedWhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val repository: UserPreferencesRepository,
    private val settingFlavorBehavior: SettingFlavorBehavior
) : ViewModel() {

    val state = repository.getDarkMode()
        .mapLatest {
            it ?: DarkMode.default
        }
        .catch { emit(DarkMode.default) }
        .stateIn(
            scope = viewModelScope,
            started = sharingStartedWhileSubscribed(),
            initialValue = DarkMode.default
        )

    val language = repository.getLanguage()
        .catch { emit(Language.default) }
        .stateIn(
            scope = viewModelScope,
            started = sharingStartedWhileSubscribed(),
            initialValue = Language.default
        )

    fun toggleDarkMode() {
        viewModelScope.launch {
            repository.toggleDarkMode()
        }
    }

    fun setl(l: Language) {
        viewModelScope.launch {
            repository.seLanguage(l)
        }

    }

    fun reset() {
        viewModelScope.launch {
            repository.reset()
        }
    }
}
