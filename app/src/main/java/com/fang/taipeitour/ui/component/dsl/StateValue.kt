package com.fang.taipeitour.ui.component.dsl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <T> StateFlow<T>.stateValue(context: CoroutineContext = EmptyCoroutineContext) = collectAsState(context).value
