package com.fang.taipeitour.ui.screen.home.urlintroduction

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UrlIntroductionViewModel(

) : ViewModel() {

    private val _show = MutableStateFlow(false)
    val show = _show.asStateFlow()

    fun set(it: Boolean) {
        _show.value = it
    }
}
