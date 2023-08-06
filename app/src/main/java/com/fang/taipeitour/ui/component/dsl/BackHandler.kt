package com.fang.taipeitour.ui.component.dsl

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.fang.taipeitour.dsl.Action
import com.fang.taipeitour.dsl.Invoke

/**
 * For the usage of state loss when onResume
 * @see [androidx.activity.compose.BackHandler]
 */
@Composable
fun BackHandler(onEvent: Action<Lifecycle.Event> = {}, onBack: Invoke) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    SideEffect {
        backCallback.isEnabled = true
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        // Add callback to the backDispatcher
        val observer = LifecycleEventObserver { _, event ->
            onEvent(event)
            when (event) {
                Lifecycle.Event.ON_PAUSE -> backCallback.remove()
                Lifecycle.Event.ON_RESUME -> {
                    backDispatcher.addCallback(lifecycleOwner, backCallback)
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            backCallback.remove()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
