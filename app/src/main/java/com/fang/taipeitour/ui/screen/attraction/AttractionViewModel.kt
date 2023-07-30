package com.fang.taipeitour.ui.screen.attraction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fang.taipeitour.data.attraction.AttractionRepository
import com.fang.taipeitour.model.attraction.Attraction2
import com.fang.taipeitour.model.attraction.convert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AttractionViewModel(

) : ViewModel() {

    var currentPage = 1

    sealed class Item {
        data class Data(val sdfsdf: Attraction2) : Item()
        object Hint : Item()
    }

    private val repository = AttractionRepository()
    private val _state = MutableStateFlow<List<Item>?>(null)
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    // 每次先存30比，一進來app先刷新(刷新最多三秒，刷新的時候不給用戶滑動??想一下)
    // 錯誤處理，如果取資料錯誤，秀出 snack bar
    fun refresh() {
        viewModelScope.launch {
            _state.value = get(1).map { Item.Data(it) } + Item.Hint
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            _state.update {
                val new = get(++currentPage).map { Item.Data(it) }
                val valid = if (new.size < 30) {
                    new
                } else {
                    new + Item.Hint
                }
                (it?.filterIsInstance<Item.Data>().orEmpty()) + valid
            }
        }
    }

    private suspend fun get(page: Int) =
        withContext(Dispatchers.IO) {
            repository.invoke(page = page).data.map { convert(it) }
        }

}
