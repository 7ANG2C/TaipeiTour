package com.fang.taipeitour.ui.screen.attraction.guide

import androidx.lifecycle.ViewModel
import com.fang.taipeitour.model.attraction.Attraction2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AttractionGuideViewModel(
    id: Attraction2?
) : ViewModel() {

    private val _state = MutableStateFlow<Attraction2?>(null)
    val state = _state.asStateFlow()

    private val _offset = MutableStateFlow<Int?>(null)
    val offset = _offset.asStateFlow()

    init {
        _state.value = id
    }
}
