package com.fang.taipeitour.ui.screen.home.webintroduction

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebIntroductionViewModel : ViewModel() {
    private val _titleState = MutableStateFlow("")
    val titleState = _titleState.asStateFlow()

    private val _urlState = MutableStateFlow("")
    val urlState = _urlState.asStateFlow()

    private val _loadingState = MutableStateFlow(0)
    val loadingState = _loadingState.asStateFlow()

    fun setTitle(title: String) {
        _titleState.value = title
    }

    fun setUrl(url: String) {
        _urlState.value = url
    }

    fun setLoadingProgress(progress: Int) {
        _loadingState.value = progress
    }
}
