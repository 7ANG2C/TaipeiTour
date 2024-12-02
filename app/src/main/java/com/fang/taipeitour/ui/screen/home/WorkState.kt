package com.fang.taipeitour.ui.screen.home

sealed class WorkState {
    data object Pending : WorkState()

    data object NoMoreData : WorkState()

    data class Error(val t: Throwable) : WorkState()
}
