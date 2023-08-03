package com.fang.taipeitour.extension

import android.util.Log
import com.fang.taipeitour.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.stateIn(
    scope: CoroutineScope,
    initialValue: T,
    stopTimeoutMillis: Long = 5_000,
) = stateIn(scope, SharingStarted.WhileSubscribed(stopTimeoutMillis), initialValue)