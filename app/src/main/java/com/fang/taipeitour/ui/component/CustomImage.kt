package com.fang.taipeitour.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun CustomImage(@DrawableRes res: Int, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = res),
        contentDescription = null,
    )
}
