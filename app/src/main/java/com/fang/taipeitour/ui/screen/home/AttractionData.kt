package com.fang.taipeitour.ui.screen.home

data class AttractionData(
    val items: List<Item>,
    val state: State,
) {
    sealed class State {
        /**
         * Success But Might Be More Data
         */
        data object MightBeMoreData : State()

        /**
         * Success With No More Data
         */
        data object NoMoreData : State()

        data class Error(val t: Throwable) : State()
    }

    val attractions get() = items.filterIsInstance<Item.Data>().map { it.attraction }
    val loadingItem get() = items.filterIsInstance<Item.Loading>().singleOrNull()
    val isError = state is State.Error
}
