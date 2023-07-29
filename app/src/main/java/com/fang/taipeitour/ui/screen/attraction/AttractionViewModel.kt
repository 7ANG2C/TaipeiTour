package com.fang.taipeitour.ui.screen.attraction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.attraction.AttractionRepository
import com.fang.taipeitour.model.attraction.Attraction2
import com.fang.taipeitour.model.attraction.convert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AttractionViewModel(

) : ViewModel() {

    sealed class Item {
        data class Data(val sdfsdf: List<Attraction2>) : Item()
        object Hint : Item()
    }

    private val repository = AttractionRepository()
    private val _state = MutableStateFlow<List<Attraction2>?>(null)
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = get(1)
        }
    }

    fun loadMore() {

    }

    fun search(query: String) {

    }

    fun scrollToTop() {

    }

    private suspend fun get(page: Int) =
        withContext(Dispatchers.IO) {
            repository.invoke(page = 1).data.map { convert(it) }
        }

}
