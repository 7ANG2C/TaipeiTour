package com.fang.taipeitour.ui.screen.attraction.guide

import androidx.lifecycle.ViewModel
import com.fang.taipeitour.model.attraction.Attraction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AttractionGuideViewModel(
    attraction: Attraction
) : ViewModel() {

    private val _state = MutableStateFlow(attraction)
    val state = _state.asStateFlow()

    private val _offset = MutableStateFlow<Int?>(null)
    val offset = _offset.asStateFlow()

}
