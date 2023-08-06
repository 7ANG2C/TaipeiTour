package com.fang.taipeitour.ui.screen.home

sealed class WorkState {
    object Pending : WorkState()
    object NoMoreData : WorkState()
    data class Error(val t: Throwable) : WorkState()
}
