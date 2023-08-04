package com.fang.taipeitour.ui.screen.home

data class AttractionData(
    val items: List<Item>,
    val state: State,
    val page: Int,
) {
    enum class State {
        SUCCESS,
        SUCCESS_WITH_NO_MORE_DATA,
        FAILURE,
        ;
    }

    val attractions get() = items.filterIsInstance<Item.Data>().map { it.attraction }
    val loadingItem get() = items.filterIsInstance<Item.Loading>().singleOrNull()
}
