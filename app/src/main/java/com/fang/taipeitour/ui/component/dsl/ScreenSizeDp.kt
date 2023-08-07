package com.fang.taipeitour.ui.component.dsl

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

val screenHeightDp @Composable get() = LocalConfiguration.current.screenHeightDp
val screenWidthDp @Composable get() = LocalConfiguration.current.screenWidthDp
