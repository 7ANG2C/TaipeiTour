package com.fang.taipeitour.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.local.UserPreferences
import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.extension.stateIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

class MainViewModel(
    repository: UserPreferencesRepository,
) : ViewModel() {
    val preferencesState =
        repository()
            .flowOn(Dispatchers.Default)
            .catch {
                emit(UserPreferences.default)
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = null,
            )
}
