package com.fang.taipeitour.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.model.DarkMode
import com.fang.taipeitour.ui.component.sharingStartedWhileSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(
    dataStore: UserPreferencesRepository
) : ViewModel() {

    val darkModeState = dataStore.getDarkMode()
        .mapLatest {
            it ?: DarkMode.default
        }
        .catch { emit(DarkMode.default) }
        .stateIn(
            scope = viewModelScope,
            started = sharingStartedWhileSubscribed(),
            initialValue = DarkMode.default
        )
}
