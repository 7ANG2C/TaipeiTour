package com.fang.taipeitour.util

import kotlinx.coroutines.flow.SharingStarted

fun sharingStartedWhileSubscribed(stopTimeoutMillis: Long = 5_000) =
    SharingStarted.WhileSubscribed(stopTimeoutMillis)
