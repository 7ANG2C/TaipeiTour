package com.fang.taipeitour.model

/**
 *
 */
sealed class WorkingState {
    object Pending : WorkingState()
    object Loading : WorkingState()
    data class Failure(val t: Throwable) : WorkingState()
    object Finished : WorkingState()
}