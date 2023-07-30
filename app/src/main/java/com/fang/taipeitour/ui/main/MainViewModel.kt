package com.fang.taipeitour.ui.main

import androidx.lifecycle.ViewModel
import com.fang.taipeitour.datastore.user.UserPreferencesDataStore

class MainViewModel(
    private val dataStore: UserPreferencesDataStore
) : ViewModel() {

    init {
        dataStore.invoke()
    }
}
