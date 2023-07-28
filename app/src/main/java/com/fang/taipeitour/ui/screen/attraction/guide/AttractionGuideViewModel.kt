package com.fang.taipeitour.ui.screen.attraction.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.attraction.AttractionRepository
import com.fang.taipeitour.service.Attraction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AttractionGuideViewModel(

) : ViewModel() {

    private val _state = MutableStateFlow<List<Attraction.Data>?>(null)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = withContext(Dispatchers.IO) {
                AttractionRepository().invoke().data
            }
        }
    }

}
