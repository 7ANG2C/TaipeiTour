package com.fang.taipeitour.ui.screen.home.attraction

import androidx.lifecycle.ViewModel
import com.fang.taipeitour.model.attraction.Attraction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AttractionViewModel(
    attraction: Attraction
) : ViewModel() {

    private val _attractionState = MutableStateFlow(attraction)
    val attractionState = _attractionState.asStateFlow()
}
