package com.fang.taipeitour.ui.component

import kotlinx.coroutines.flow.SharingStarted

fun sharingStartedWhileSubscribed(stopTimeoutMillis: Long = 5_000) =
    SharingStarted.WhileSubscribed(stopTimeoutMillis)
