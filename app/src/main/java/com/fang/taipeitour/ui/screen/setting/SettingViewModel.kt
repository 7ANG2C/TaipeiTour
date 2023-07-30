package com.fang.taipeitour.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.datastore.user.UserPreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _state = MutableStateFlow<Setting?>(null)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesDataStore.invoke()
                .flowOn(Dispatchers.Default)
                .collectLatest {
                    _state.value = null
                }
        }
    }

}
