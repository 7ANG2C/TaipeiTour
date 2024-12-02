package com.fang.taipeitour.ui.screen.setting

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.extension.stateIn
import com.fang.taipeitour.flavor.PreviewFunctionFlavor
import com.fang.taipeitour.model.language.Language
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SettingViewModel(
    private val repository: UserPreferencesRepository,
    private val previewFunctionFlavor: PreviewFunctionFlavor,
) : ViewModel() {
    val previewFunctions =
        snapshotFlow {
            previewFunctionFlavor()
        }
            .catch { emit(emptyList()) }
            .stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
            )

    val isEmpty =
        repository.isDefault()
            .catch { emit(true) }
            .stateIn(
                scope = viewModelScope,
                initialValue = true,
            )

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            repository.seLanguage(language)
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            repository.toggleDarkMode()
        }
    }

    fun toggleColorScheme() {
        viewModelScope.launch {
            repository.toggleColorScheme()
        }
    }

    fun resetUserPreferences() {
        viewModelScope.launch {
            repository.resetUserPreferences()
        }
    }
}
