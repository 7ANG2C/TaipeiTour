package com.fang.taipeitour.ui.component

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.findFragment

@Composable
fun <T : Fragment> FragmentContainer(
    modifier: Modifier = Modifier,
    fragment: T,
    update: T?.() -> Unit
) {
    val localView = LocalView.current
    val currentContext = LocalContext.current
    val fragmentManager = remember(localView) {
        runCatching {
            localView.findFragment<T>().childFragmentManager
        }.getOrDefault(
            (currentContext as? FragmentActivity)?.supportFragmentManager
        )
    }

    val containerId by remember {
        mutableStateOf(View.generateViewId())
    }

    @Suppress("UNCHECKED_CAST")
    val getFragment = {
        fragmentManager?.findFragmentById(containerId) as? T
    }

    AndroidView(
        factory = { context ->
            val containerView = FragmentContainerView(context).apply {
                id = containerId
            }
            fragmentManager?.commit {
                replace(containerView.id, fragment)
            }
            containerView
        },
        modifier = modifier,
    ) {
        update(getFragment())
    }

    DisposableEffect(localView, currentContext, containerId) {
        onDispose {
            val currentFragment = getFragment()
            if (currentFragment?.isStateSaved == false) {
                fragmentManager?.commit { remove(currentFragment) }
            }
        }
    }
}
